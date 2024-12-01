package org.example.laboration2backend.controller;

import lombok.extern.slf4j.Slf4j;

import org.example.laboration2backend.apiauth.ApiKeyAuthService;
import org.example.laboration2backend.category.CategoryService;
import org.example.laboration2backend.dto.CategoryDto;
import org.example.laboration2backend.dto.PlaceDto;
import org.example.laboration2backend.entity.*;
import org.example.laboration2backend.exceptions.ResourceNotFoundException;
import org.example.laboration2backend.place.PlaceService;
import org.example.laboration2backend.playground.PlaygroundRepository;
import org.example.laboration2backend.user.AppUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class InfoController {

    private final ApiKeyAuthService apiKeyAuthService;
    PlaceService placeService;
    CategoryService categoryService;
    PlaygroundRepository playgroundRepository;
     AppUserRepository appUserRepository;

    public InfoController(CategoryService categoryService, PlaceService placeService,
                          ApiKeyAuthService apiKeyAuthService, AppUserRepository appUserRepository) {

        this.categoryService = categoryService;
        this.placeService = placeService;
        this.apiKeyAuthService = apiKeyAuthService;
        this.appUserRepository = appUserRepository;
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
   public List<PlaceDto> getMyPlaces() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username;
        if (authentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            username = authentication.getPrincipal().toString(); }
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        int userId = appUser.getId();
         log.info("userId is: " + userId);
        List<Place> places = placeService.getPlacesByUserId(userId);
        return places.stream().map(PlaceDto::fromPlace).toList();
}

    //Hämta alla publika platser eller en specifik publik plats (för anonyma användare).
    @GetMapping("/place")
    public List<PlaceDto> publicPlaces(){
        List<Place> places = placeService.getPublicPlaces();
        return places.stream().map(PlaceDto::fromPlace).collect(Collectors.toList()); }

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

    @PutMapping("/place/{placeId}")
    public ResponseEntity<Void> updatePlace(@PathVariable Integer placeId, @RequestBody PlaceDto placeDto){
        try {
            placeService.updatePlace(placeId, placeDto);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }


    @GetMapping("/place/active")
    public List<PlaceDto> publicActivePlaces(){
        return placeService.getPublicActivePlaces().stream()
                .map(PlaceDto::fromPlace)
                .collect(Collectors.toList());
    }

    @GetMapping("/place/inactive")
    public List<PlaceDto> publicInactivePlaces(){
        return placeService.getPublicInactivePlaces().stream()
                .map(PlaceDto::fromPlace)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/place/{placeId}")
    public ResponseEntity<Void> deletePlace(@PathVariable Integer placeId) {
        try {
            placeService.deletePlace(placeId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/test")
    @PreAuthorize("hasAuthority('read:test')")
    public Collection<ApiKey> test(){
         return apiKeyAuthService.getMyApiKeys();
    }
}
