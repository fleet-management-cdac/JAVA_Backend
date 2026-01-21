package com.example.controllers;

import com.example.dto.AirportMasterDTO;
import com.example.service.AirportMasterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations/airports")
@CrossOrigin(origins = "*")
public class AirportMasterController {

    private final AirportMasterService airportMasterService;

    public AirportMasterController(AirportMasterService airportMasterService) {
        this.airportMasterService = airportMasterService;
    }

    // Get All: http://localhost:8080/api/locations/airports
    @GetMapping
    public ResponseEntity<List<AirportMasterDTO>> getAllAirports() {
        return ResponseEntity.ok(airportMasterService.getAllAirports());
    }

    // Get by State: http://localhost:8080/api/locations/airports/state/{stateId}
    @GetMapping("/state/{stateId}")
    public ResponseEntity<List<AirportMasterDTO>> getAirportsByState(@PathVariable Long stateId) {
        return ResponseEntity.ok(airportMasterService.getAirportsByState(stateId));
    }

    // Get by Code: http://localhost:8080/api/locations/airports/code/{code}
    @GetMapping("/code/{code}")
    public ResponseEntity<AirportMasterDTO> getAirportByCode(@PathVariable String code) {
        return ResponseEntity.ok(airportMasterService.getAirportByCode(code));
    }
}