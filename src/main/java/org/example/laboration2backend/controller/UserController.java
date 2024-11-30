package org.example.laboration2backend.controller;

import org.example.laboration2backend.entity.AppUser;
import org.example.laboration2backend.user.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping
    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    @GetMapping("/{username}")
    public ResponseEntity<AppUser> getUserByUsername(@PathVariable String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(appUser); }
}
