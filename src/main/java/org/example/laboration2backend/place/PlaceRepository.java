package org.example.laboration2backend.place;

import org.example.laboration2backend.entity.Place;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface PlaceRepository extends ListCrudRepository<Place, Integer> {

      Optional<Place> findByName(String name);

      List<Place> findByCategoryIdAndPublicStatus(Integer categoryId, Boolean publicStatus);

      List<Place> findByPublicStatus(Boolean publicStatus);

      // Fetch all places by user ID (both public and private)
      List<Place> findByUserId(Integer userId);

      @Query("SELECT p FROM Place p WHERE p.deleted = false AND p.id = :placeId")
      Optional<Place> findActiveById(@Param("placeId") Integer placeId);

      @Query("SELECT p FROM Place p WHERE p.deleted = false")
      List<Place> findAllActivePlaces();

      @Query("SELECT p FROM Place p WHERE p.deleted = true")
      List<Place> findAllInactivePlaces();
}
