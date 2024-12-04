package org.example.laboration2backend.dto;

import org.example.laboration2backend.entity.Place;

public class PlaceMapper {

    private PlaceMapper(){
        throw new UnsupportedOperationException("Utility class");
    }

    public static void mapToPlace(PlaceDto placeDto, Place place) {
        place.setName(placeDto.name());
        place.setPublicStatus(placeDto.published());
        place.setLastChange(placeDto.lastChange());
        place.setDescription(placeDto.description());
        place.setCreatedTime(placeDto.createdTime());
        place.setDeleted(placeDto.deleted());
    }
}
