package org.example.laboration2backend.dto;

import org.example.laboration2backend.entity.Place;

import java.math.BigDecimal;
import java.time.Instant;

public record PlaceDto(
        String name, Integer categoryId, Integer userId,
        Boolean published, Instant lastChange, String description,
        BigDecimal latitude, BigDecimal longitude, Instant createdTime,
        Boolean deleted) {
    public static PlaceDto fromPlace(Place place) {
        return new PlaceDto(
                place.getName(),
                place.getCategory() != null ? place.getCategory().getId() : null,
                place.getUserId(),
                place.getPublicStatus(),
                place.getLastChange(),
                place.getDescription(),
                place.getLatitude(),
                place.getLongitude(),
                place.getCreatedTime(),
                place.getDeleted()

        );
    }
}
