package com.example.controllers;

import com.example.dto.CityDto;
import com.example.service.CityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@CrossOrigin(origins = "http://localhost:3000")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public List<CityDto> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/by-state/{stateId}")
    public List<CityDto> getCitiesByState(@PathVariable Long stateId) {
        return cityService.getCitiesByState(stateId);
    }
}