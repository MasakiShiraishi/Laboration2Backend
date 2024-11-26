package org.example.laboration2backend.dto;

import org.example.laboration2backend.entity.Place;

public class PlaceMapper {

    private PlaceMapper(){
        throw new UnsupportedOperationException("Utility class");
    }

    public static void mapToPlace(PlaceDto placeDto, Place place) {
        place.setName(placeDto.name());
        place.setUserId(placeDto.userId());
        place.setPublicStatus(placeDto.published());
        place.setLastChange(placeDto.lastChange());
        place.setDescription(placeDto.description());
        place.setLatitude(placeDto.latitude());
        place.setLongitude(placeDto.longitude());
        place.setCreatedTime(placeDto.createdTime());
    }
}
