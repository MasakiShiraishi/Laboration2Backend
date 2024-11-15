package org.example.laboration2backend.controller;

import org.example.laboration2backend.category.Category;
import org.example.laboration2backend.category.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InfoController {

    CategoryService categoryService;

    public InfoController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String info(){
        return "This is a PlatsController";
    }

    @GetMapping("/category")
    public List<Category> getAllCategories(){
               return categoryService.allCategories();
    }
}
