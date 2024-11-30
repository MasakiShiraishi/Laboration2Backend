package org.example.laboration2backend.dto;

import org.example.laboration2backend.entity.AppUser;
import org.example.laboration2backend.entity.Place;
import java.time.Instant;

public record PlaceDto(
        String name, Integer categoryId, Integer appUserId,
        Boolean published, Instant lastChange, String description,
        Integer playgroundId ,Instant createdTime,
        Boolean deleted) {
    public static PlaceDto fromPlace(Place place) {
        return new PlaceDto(
                place.getName(),
                place.getCategory() != null ? place.getCategory().getId() : null,
                place.getAppUser() != null ? place.getAppUser().getId() : null,
                place.getPublicStatus(),
                place.getLastChange(),
                place.getDescription(),
                place.getPlayground() != null ? place.getPlayground().getId() : null,
                place.getCreatedTime(),
                place.getDeleted()

        );
    }

}
