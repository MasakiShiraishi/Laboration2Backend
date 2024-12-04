package org.example.laboration2backend.place;

import lombok.extern.slf4j.Slf4j;
import org.example.laboration2backend.category.CategoryRepository;
import org.example.laboration2backend.dto.PlaceDto;
import org.example.laboration2backend.dto.PlaceMapper;
import org.example.laboration2backend.entity.AppUser;
import org.example.laboration2backend.entity.Category;
import org.example.laboration2backend.entity.Place;
import org.example.laboration2backend.entity.Playground;
import org.example.laboration2backend.playground.PlaygroundRepository;
import org.example.laboration2backend.security.CheckUserAuthorization;
import org.example.laboration2backend.user.AppUserRepository;
import org.springframework.security.core.Authentication;
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
    PlaygroundRepository playgroundRepository;
    CheckUserAuthorization checkUserAuthorization;
    AppUserRepository appUserRepository;

    public PlaceService(PlaceRepository placeRepository, CategoryRepository categoryRepository,
                        PlaygroundRepository playgroundRepository, CheckUserAuthorization checkUserAuthorization,
                        AppUserRepository appUserRepository) {
        this.placeRepository = placeRepository;
        this.categoryRepository = categoryRepository;
        this.playgroundRepository = playgroundRepository;
        this.checkUserAuthorization = checkUserAuthorization;
        this.appUserRepository = appUserRepository;
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
    return placeRepository.findByAppUserId(userId);
}
    @Transactional
   public int addPlace(PlaceDto placeDto) {
       placeRepository.findByName(placeDto.name())
               .ifPresent(existingPlace -> {
                   throw new IllegalArgumentException("A place with the name '" + placeDto.name() + "' already exists.");
               });
       Place place = new Place();
       PlaceMapper.mapToPlace(placeDto, place);
       Category category = getCategoryById(placeDto.categoryId());
       place.setCategory(category);
       Playground playground = playgroundRepository.
               findById(placeDto.playgroundId()) .orElse(null);
       place.setPlayground(playground);

       AppUser appUser = appUserRepository.findById(placeDto.appUserId()).orElse(null);
       place.setAppUser(appUser);
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

    public void updatePlace(int placeId, PlaceDto placeDto, String username) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Place not found for ID: " + placeId));

        checkUserAuthorization.checkUserAuthorization(place.getAppUser());

        placeRepository.findByName(placeDto.name())
                .ifPresent(existingPlace -> {
                    if (!existingPlace.getId().equals(placeId)) {
                        throw new IllegalArgumentException("A place with the name '"
                                + placeDto.name() + "' already exists.");}
                });

        PlaceMapper.mapToPlace(placeDto, place);
        Category category = getCategoryById(placeDto.categoryId());
        place.setCategory(category);
        Playground playground = playgroundRepository.
                findById(placeDto.playgroundId()) .orElse(null);
        place.setPlayground(playground);
        placeRepository.save(place);
        log.info("Place with id {} has been updated by {}", placeId, username);
    }

         @Transactional
        public void deletePlace(Integer placeId, String username) throws ResourceNotFoundException {
            // Only records with Deleted flag set to false are included
            Place place = placeRepository.findActiveById(placeId).orElseThrow(
                    () -> new ResourceNotFoundException("Place not found with id: " + placeId));

            checkUserAuthorization.checkUserAuthorization(place.getAppUser());

            place.setDeleted(true);
            placeRepository.save(place);
            log.info("Place with id {} has been marked as deleted by {}", placeId, username);
        }
    }



