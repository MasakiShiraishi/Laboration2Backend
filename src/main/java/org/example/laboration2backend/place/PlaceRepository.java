package org.example.laboration2backend.place;

import org.example.laboration2backend.entity.Place;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;


public interface PlaceRepository extends ListCrudRepository<Place, Integer> {

      List<Place> findByCategoryIdAndPublicStatus(Integer categoryId, Boolean publicStatus);

      List<Place> findByPublicStatus(Boolean publicStatus);
}
