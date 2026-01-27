package com.example.controllers;

import com.example.dto.VehicleDTO;
import com.example.dto.VehicleTypeWithRatesDTO;
import com.example.service.VehicleService;
import com.example.service.VehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {

    @Autowired
    private VehicleTypeService vehicleTypeService;
    @Autowired
    private VehicleService vehicleService;
    /**
     * GET /api/vehicles/types-with-rates
     * Returns all vehicle types with their daily, weekly, monthly rates
     */
    @GetMapping("/types-with-rates")
    public ResponseEntity<List<VehicleTypeWithRatesDTO>> getVehicleTypesWithRates() {
        List<VehicleTypeWithRatesDTO> vehicleTypes = vehicleTypeService.getAllVehicleTypesWithRates();
        return ResponseEntity.ok(vehicleTypes);
    }
    @GetMapping("/available-for-handover")
    public List<VehicleDTO> getAvailableVehicles(
            @RequestParam(required = false) Long hubId,
            @RequestParam(required = false) Long vehicleTypeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate pickupDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate) {

        return vehicleService.getAvailableVehiclesForHandover(hubId, vehicleTypeId, pickupDate, returnDate);
    }

}
