package com.example.controllers;

import com.example.dto.VehicleInventoryDTO;
import com.example.service.VehicleInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class VehicleInventoryController {

    @Autowired
    private VehicleInventoryService vehicleInventoryService;

    /**
     * Get all vehicles in a specific hub (available or not)
     * GET /api/inventory/hub/{hubId}
     */
    @GetMapping("/hub/{hubId}")
    public ResponseEntity<List<VehicleInventoryDTO>> getAllVehiclesInHub(
            @PathVariable Long hubId) {
        List<VehicleInventoryDTO> inventory = vehicleInventoryService.getHubInventory(hubId);
        return ResponseEntity.ok(inventory);
    }

    /**
     * Get all vehicles across all hubs
     * GET /api/inventory/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<VehicleInventoryDTO>> getAllVehicles() {
        List<VehicleInventoryDTO> inventory = vehicleInventoryService.getAllVehicles();
        return ResponseEntity.ok(inventory);
    }

    /**
     * Get vehicles by type in a hub
     * GET /api/inventory/hub/{hubId}/type/{vehicleTypeId}
     */
    @GetMapping("/hub/{hubId}/type/{vehicleTypeId}")
    public ResponseEntity<List<VehicleInventoryDTO>> getVehiclesByHubAndType(
            @PathVariable Long hubId,
            @PathVariable Long vehicleTypeId) {
        List<VehicleInventoryDTO> inventory = vehicleInventoryService.getVehiclesByHubAndType(hubId, vehicleTypeId);
        return ResponseEntity.ok(inventory);
    }
}