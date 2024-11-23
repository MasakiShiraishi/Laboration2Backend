package org.example.laboration2backend.controller;

import lombok.extern.slf4j.Slf4j;

import org.example.laboration2backend.apiauth.ApiKeyAuthService;
import org.example.laboration2backend.category.CategoryService;
import org.example.laboration2backend.dto.CategoryDto;
import org.example.laboration2backend.dto.PlaceDto;
import org.example.laboration2backend.entity.ApiKey;
import org.example.laboration2backend.entity.Place;
import org.example.laboration2backend.place.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
public class InfoController {

    private final ApiKeyAuthService apiKeyAuthService;
    PlaceService placeService;
    CategoryService categoryService;

    public InfoController(CategoryService categoryService, PlaceService placeService, ApiKeyAuthService apiKeyAuthService){

        this.categoryService = categoryService;
        this.placeService = placeService;
        this.apiKeyAuthService = apiKeyAuthService;
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
    // Hämta alla platser (både publika och privata) som tillhör den inloggade användaren.
    @GetMapping("/places/user")
    public List<Place> getMyPlaces(Principal principal) {
    int userId = Integer.parseInt(principal.getName().substring(4)); // Extract ID from "user"
    log.info("userId is: " + userId);
    return placeService.getPlacesByUserId(userId);
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
    public Collection<ApiKey> test(){
         return apiKeyAuthService.getMyApiKeys();
    }
}
