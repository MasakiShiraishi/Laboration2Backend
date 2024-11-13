package org.example.laboration2backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlatsController {

    @GetMapping("/")
    public String info(){
        return "This is a PlatsController";
    }
}
