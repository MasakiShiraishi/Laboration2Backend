package org.example.laboration2backend.dto;

import org.example.laboration2backend.entity.Category;

public record CategoryDto(String name, String symbol, String description) {
    public static CategoryDto fromCategory(Category category) {
        return new CategoryDto(
                category.getName(),
                category.getSymbol(),
                category.getDescription());
    }
}
