package org.example.laboration2backend.controller;

import org.example.laboration2backend.category.CategoryService;
import org.example.laboration2backend.dto.CategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
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
    public List<CategoryDto> getAllCategories(){
               return categoryService.allCategories();
    }

    @PostMapping("/category")
    public ResponseEntity<Void> createCategory(@RequestBody CategoryDto categoryDto){
        int id = categoryService.addCategory(categoryDto);
        return ResponseEntity.created(URI.create("/category/" + id)).build();
    }
}
