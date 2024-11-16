package org.example.laboration2backend.category;

import org.example.laboration2backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;

public interface CategoryRepository extends ListCrudRepository<Category, Integer> {
  }