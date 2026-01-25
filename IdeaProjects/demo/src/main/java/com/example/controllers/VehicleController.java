package com.example.controllers;

import com.example.dto.VehicleTypeWithRatesDTO;
import com.example.entity.Vehicle;
import com.example.repository.VehicleRepository;
import com.example.service.VehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {

    @Autowired
    private VehicleTypeService vehicleTypeService;

    @Autowired
    private VehicleRepository vehicleRepository;

    /**
     * GET /api/vehicles/types-with-rates
     * Returns all vehicle types with their daily, weekly, monthly rates
     */
    @GetMapping("/types-with-rates")
    public ResponseEntity<List<VehicleTypeWithRatesDTO>> getVehicleTypesWithRates() {
        List<VehicleTypeWithRatesDTO> vehicleTypes = vehicleTypeService.getAllVehicleTypesWithRates();
        return ResponseEntity.ok(vehicleTypes);
    }

    /**
     * GET /api/vehicles/available?hubId=1&vehicleTypeId=2
     * Returns all available vehicles for staff handover, filtered by hub and
     * vehicle type
     */
    @GetMapping("/available")
    public ResponseEntity<List<Map<String, Object>>> getAvailableVehicles(
            @RequestParam(required = false) Long hubId,
            @RequestParam(required = false) Long vehicleTypeId) {

        List<Vehicle> vehicles;

        if (hubId != null && vehicleTypeId != null) {
            // Filter by both hub and vehicle type
            vehicles = vehicleRepository.findByStatusAndHubIdAndVehicleTypeId("available", hubId, vehicleTypeId);
        } else if (hubId != null) {
            // Filter by hub only
            vehicles = vehicleRepository.findByStatusAndHubId("available", hubId);
        } else if (vehicleTypeId != null) {
            // Filter by vehicle type only
            vehicles = vehicleRepository.findByStatusAndVehicleTypeId("available", vehicleTypeId);
        } else {
            // No filter
            vehicles = vehicleRepository.findByStatus("available");
        }

        List<Map<String, Object>> result = vehicles.stream().map(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("vehicleId", v.getId());
            map.put("company", v.getCompany());
            map.put("model", v.getModel());
            map.put("registrationNo", v.getRegistrationNo());
            map.put("vehicleTypeName", v.getVehicleType() != null ? v.getVehicleType().getTypeName() : null);
            map.put("vehicleTypeId", v.getVehicleType() != null ? v.getVehicleType().getId() : null);
            map.put("hubName", v.getHub() != null ? v.getHub().getHubName() : null);
            map.put("hubId", v.getHub() != null ? v.getHub().getId() : null);
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
