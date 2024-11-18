package org.example.laboration2backend.controller;

import org.example.laboration2backend.category.CategoryService;
import org.example.laboration2backend.dto.CategoryDto;
import org.example.laboration2backend.dto.PlaceDto;
import org.example.laboration2backend.entity.Place;
import org.example.laboration2backend.place.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class InfoController {

    PlaceService placeService;
    CategoryService categoryService;

    public InfoController(CategoryService categoryService, PlaceService placeService){

        this.categoryService = categoryService;
        this.placeService = placeService;
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

    @GetMapping("/place")
    public List<Place> publicPlaces(){
        return placeService.getPublicPlaces();
    }

    @PostMapping("/place")
    public ResponseEntity<Void> createPlace(@RequestBody PlaceDto placeDto){
        int id = placeService.addPlace(placeDto);
        return ResponseEntity.created(URI.create("/place/" + id)).build();
    }

    //Hämta alla publika platser inom en specifik kategori.
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PlaceDto>> getPublicPlacesByCategory(@PathVariable Integer categoryId){
        List<Place> places = placeService.getPublicPlacesByCategory(categoryId);
                List<PlaceDto> placeDtos = places.stream()
                .map(PlaceDto::fromPlace)
                .toList();

        return ResponseEntity.ok(placeDtos);
    }
}
