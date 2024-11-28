package org.example.laboration2backend.dto;

import org.example.laboration2backend.entity.Place;
import org.example.laboration2backend.entity.Playground;
import org.example.laboration2backend.entity.Category;
import java.time.Instant;

public record PlaceDto(
        String name, Integer categoryId, Integer userId,
        Boolean published, Instant lastChange, String description,
        Integer playgroundId ,Instant createdTime,
        Boolean deleted) {
    public static PlaceDto fromPlace(Place place) {
        return new PlaceDto(
                place.getName(),
                place.getCategory() != null ? place.getCategory().getId() : null,
                place.getUserId(),
                place.getPublicStatus(),
                place.getLastChange(),
                place.getDescription(),
                place.getPlayground() != null ? place.getPlayground().getId() : null,
                place.getCreatedTime(),
                place.getDeleted()

        );
    }

    public static Place toPlace(PlaceDto placeDto, Category category, Playground playground) {
        Place place = new Place();
        place.setName(placeDto.name());
        place.setCategory(category);
        place.setUserId(placeDto.userId());
        place.setPublicStatus(placeDto.published());
        place.setLastChange(placeDto.lastChange());
        place.setDescription(placeDto.description());
        place.setPlayground(playground);
        place.setCreatedTime(placeDto.createdTime());
        place.setDeleted(placeDto.deleted());
        return place; }
}
