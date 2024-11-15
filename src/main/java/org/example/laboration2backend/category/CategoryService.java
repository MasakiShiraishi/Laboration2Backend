package org.example.laboration2backend.category;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    public List<Category> allCategories() {
        return List.of(
                new Category("Park"),
                new Category("Restarang")
        );
    }
}
