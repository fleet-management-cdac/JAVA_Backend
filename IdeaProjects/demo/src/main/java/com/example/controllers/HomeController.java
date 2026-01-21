package com.example.controller;

import com.example.dto.StateDto;
import com.example.service.HomeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/home")
@CrossOrigin(origins = "http://localhost:3000")
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/states")
    public List<StateDto> getStates() {
        return homeService.getAllStates();
    }
}