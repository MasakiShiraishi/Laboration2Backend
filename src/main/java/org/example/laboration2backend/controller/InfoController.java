package org.example.laboration2backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.laboration2backend.category.CategoryService;
import org.example.laboration2backend.dto.CategoryDto;
import org.example.laboration2backend.dto.PlaceDto;
import org.example.laboration2backend.entity.Place;
import org.example.laboration2backend.place.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
public class InfoController {

    PlaceService placeService;
    CategoryService categoryService;

    public InfoController(CategoryService categoryService, PlaceService placeService){

        this.categoryService = categoryService;
        this.placeService = placeService;
    }

    @GetMapping("/")
    public String info(){
        log.info("testtest");
        return "This is a PlatsController";
    }

    @GetMapping("/category")
    public List<CategoryDto> getAllCategories(){
               return categoryService.allCategories();
    }

    //Skapa en ny kategori (kräver adminroll). Namnet får inte kollidera med en befintlig kategori.
    @PostMapping("/category")
    public ResponseEntity<Void> createCategory(@RequestBody CategoryDto categoryDto){
        int id = categoryService.addCategory(categoryDto);
        return ResponseEntity.created(URI.create("/category/" + id)).build();
    }

    //Hämta alla publika platser eller en specifik publik plats (för anonyma användare).
    @GetMapping("/place")
    public List<Place> publicPlaces(){
        return placeService.getPublicPlaces();
    }

    //Skapa en ny plats (kräver inloggning).
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
        log.info("getPublicPlacesByCategory method called with categoryId: {}", categoryId);

        return ResponseEntity.ok(placeDtos);
    }
    @GetMapping("/api/test")
    @PreAuthorize("hasAuthority('read:test')")
    @PostFilter("filterObject.name() == authentication.name")
    public Collection<Test> test(Principal principal){
        return new ArrayList(List.of(
                new Test("A123B","This info belongs to A123B"),
                new Test("A123B","This info also belongs to A123B"),
                new Test("AB","This info belongs to AB"),
                new Test("Test","This info belongs to Test")
        ));
    }

    public record Test(String name, String info){

    }
}
