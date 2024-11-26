package org.example.laboration2backend.place;

import lombok.extern.slf4j.Slf4j;
import org.example.laboration2backend.category.CategoryRepository;
import org.example.laboration2backend.dto.PlaceDto;
import org.example.laboration2backend.dto.PlaceMapper;
import org.example.laboration2backend.entity.Category;
import org.example.laboration2backend.entity.Place;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.example.laboration2backend.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Slf4j
public class PlaceService {

    PlaceRepository placeRepository;
    CategoryRepository categoryRepository;
    public PlaceService(PlaceRepository placeRepository, CategoryRepository categoryRepository) {
        this.placeRepository = placeRepository;
        this.categoryRepository = categoryRepository;
    }

    private Category getCategoryById(int categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found for ID: " + categoryId));
    }

   public List<PlaceDto> allPlaces() {
        return placeRepository.findAll().stream()
                .map(PlaceDto::fromPlace)
                .toList();
   }

public List<Place> getPlacesByUserId(int userId) {
    return placeRepository.findByUserId(userId);
}

   public int addPlace(PlaceDto placeDto) {
       placeRepository.findByName(placeDto.name())
               .ifPresent(existingPlace -> {
                   throw new IllegalArgumentException("A place with the name '" + placeDto.name() + "' already exists.");
               });

       Place place = new Place();
       place.setName(placeDto.name());
       Category category = categoryRepository.findById(placeDto.categoryId())
               .orElseThrow(() -> new IllegalArgumentException("Category not found for ID: " + placeDto.categoryId()));
       place.setCategory(category);
       place.setUserId(placeDto.userId());
       place.setPublicStatus(placeDto.published());
       place.setLastChange(placeDto.lastChange());
       place.setDescription(placeDto.description());
       place.setLatitude(placeDto.latitude());
       place.setLongitude(placeDto.longitude());
       place.setCreatedTime(placeDto.createdTime());
       place = placeRepository.save(place);
       return place.getId();
   }

   public List<Place> getPublicPlaces() {
       List<Place> places = placeRepository.findByPublicStatus(true);
       return places;
   }

   public List<Place> getPublicPlacesByCategory(Integer categoryId) {
        return placeRepository.findByCategoryIdAndPublicStatus(categoryId, true);
   }

   // get only active places
   public List<Place> getPublicActivePlaces() {
        return placeRepository.findAllActivePlaces();
   }

    public List<Place> getPublicInactivePlaces() {
        return placeRepository.findAllInactivePlaces();
    }

    public void updatePlace(int placeId, PlaceDto placeDto) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Place not found for ID: " + placeId));
        // Get the currently authenticated user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername;
        if (principal instanceof UserDetails) {
            currentUsername = ((UserDetails) principal).getUsername();
        } else {
            currentUsername = principal.toString();
        }
        // Check if the user is the owner or has ADMIN role
        String placeOwner =  "user" + place.getUserId();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        log.debug("Place owner: {}, Current user: {}, Is admin: {}", placeOwner, currentUsername, isAdmin);

        if(!isAdmin && !placeOwner.equals(currentUsername)) {
            throw new SecurityException("Unauthorized to update this place");
        }
        placeRepository.findByName(placeDto.name())
                .ifPresent(existingPlace -> {
                    if (!existingPlace.getId().equals(placeId)) {
                        throw new IllegalArgumentException("A place with the name '"
                                + placeDto.name() + "' already exists.");}
                });

        PlaceMapper.mapToPlace(placeDto, place);
        Category category = getCategoryById(placeDto.categoryId());
        place.setCategory(category);
        placeRepository.save(place);
    }

    @Transactional
    public void deletePlace(Integer placeId) throws ResourceNotFoundException {
        // Only records with Deleted file is false are included
        Place place = placeRepository.findActiveById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found with id: " + placeId));

        place.setDeleted(true);
        placeRepository.save(place);
        log.info("Place with id {} has been marked as deleted", placeId);
    }
}
