package com.example.controllers;

import com.example.dto.VehicleTypeWithRatesDTO;
import com.example.service.VehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {

    @Autowired
    private VehicleTypeService vehicleTypeService;

    /**
     * GET /api/vehicles/types-with-rates
     * Returns all vehicle types with their daily, weekly, monthly rates
     */
    @GetMapping("/types-with-rates")
    public ResponseEntity<List<VehicleTypeWithRatesDTO>> getVehicleTypesWithRates() {
        List<VehicleTypeWithRatesDTO> vehicleTypes = vehicleTypeService.getAllVehicleTypesWithRates();
        return ResponseEntity.ok(vehicleTypes);
    }
}
