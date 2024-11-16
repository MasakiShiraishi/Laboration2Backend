package org.example.laboration2backend.category;

import org.example.laboration2backend.dto.CategoryDto;
import org.example.laboration2backend.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

//    public List<Category> allCategories() {
//        return List.of(
//                new Category("Park"),
//                new Category("Restarang")
//        );
//    }
// Get all categories
//public List<Category> AllCategories() {
//    return categoryRepository.findAll();
//}

    public List<CategoryDto> allCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryDto::fromCategory)
                .toList();
    }


    public int addCategory(CategoryDto categoryDto) {
        System.out.println("Category name: " + categoryDto.name());
        Category category = new Category();
        category.setName(categoryDto.name());
        category.setSymbol(categoryDto.symbol());
        category.setDescription(categoryDto.description());
        category = categoryRepository.save(category);
        return category.getId();
    }


}
