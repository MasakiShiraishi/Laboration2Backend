package org.example.laboration2backend.category;

import org.example.laboration2backend.dto.CategoryDto;
import org.example.laboration2backend.entity.Category;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> allCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryDto::fromCategory)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public int addCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.name());
        category.setSymbol(categoryDto.symbol());
        category.setDescription(categoryDto.description());
        category = categoryRepository.save(category);
        return category.getId();
    }
    public Category findById(Integer id) throws ResourceNotFoundException {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id)); }

}
