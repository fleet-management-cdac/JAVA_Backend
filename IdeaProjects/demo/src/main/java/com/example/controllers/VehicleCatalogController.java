package com.example.controllers;

import com.example.dto.VehicleCatalogDTO;
import com.example.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
// @CrossOrigin allows your React frontend (usually on port 3000) to communicate with this Java backend
@CrossOrigin(origins = "http://localhost:3000") 
public class VehicleCatalogController {

    @Autowired
    private VehicleService vehicleService;

    /**
     * Endpoint to fetch the list of vehicles + rates for the "Vehicle Selection" page.
     * URL: http://localhost:8080/api/catalog/available
     */
    @GetMapping("/available")
    public List<VehicleCatalogDTO> getVehicleCatalog() {
        return vehicleService.getVehicleCatalogData();
    }
}