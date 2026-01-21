package com.example.controllers;

import com.example.dto.CityMasterDTO;
import com.example.service.CityMasterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class CityMasterController {

    private final CityMasterService cityMasterService;

    public CityMasterController(CityMasterService cityMasterService) {
        this.cityMasterService = cityMasterService;
    }

    // Path: http://localhost:8080/api/locations/cities/state/1
    @GetMapping("/cities/state/{stateId}")
    public ResponseEntity<List<CityMasterDTO>> getCitiesByState(@PathVariable Long stateId) {
        List<CityMasterDTO> cities = cityMasterService.getCitiesByState(stateId);
        return ResponseEntity.ok(cities);
    }

    // Path: http://localhost:8080/api/locations/city/1
    @GetMapping("/city/{cityId}")
    public ResponseEntity<CityMasterDTO> getCityById(@PathVariable Long cityId) {
        CityMasterDTO city = cityMasterService.getCityById(cityId);
        return ResponseEntity.ok(city);
    }
}