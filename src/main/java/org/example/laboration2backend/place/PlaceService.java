package org.example.laboration2backend.place;

import org.example.laboration2backend.category.CategoryRepository;
import org.example.laboration2backend.dto.PlaceDto;
import org.example.laboration2backend.entity.Category;
import org.example.laboration2backend.entity.Place;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceService {

    PlaceRepository placeRepository;
    CategoryRepository categoryRepository;
    public PlaceService(PlaceRepository placeRepository, CategoryRepository categoryRepository) {
        this.placeRepository = placeRepository;
        this.categoryRepository = categoryRepository;
    }

   public List<PlaceDto> allPlaces() {
        return placeRepository.findAll().stream()
                .map(PlaceDto::fromPlace)
                .toList();
   }

   public int addPlace(PlaceDto placeDto) {
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
}
