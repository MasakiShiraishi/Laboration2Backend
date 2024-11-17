package org.example.laboration2backend.place;

import org.example.laboration2backend.entity.Place;
import org.springframework.data.repository.ListCrudRepository;

public interface PlaceRepository extends ListCrudRepository<Place, Integer> {
}
