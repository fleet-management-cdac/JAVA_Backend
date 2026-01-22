package com.example.controllers;

import com.example.dto.VehicleCatalogDTO;
import com.example.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@CrossOrigin(origins = "http://localhost:3000")
public class VehicleCatalogController {

    @Autowired
    private VehicleService vehicleService;

    // 1. GLOBAL: http://localhost:8080/api/catalog/available
    @GetMapping("/available")
    public List<VehicleCatalogDTO> getAllVehicles() {
        return vehicleService.getVehicleCatalogData(null);
    }

    // 2. HUB SPECIFIC: http://localhost:8080/api/catalog/available/1
    @GetMapping("/available/{hubId}")
    public List<VehicleCatalogDTO> getVehiclesByHub(@PathVariable Long hubId) {
        return vehicleService.getVehicleCatalogData(hubId);
    }
}