package com.example.controllers;

import com.example.dto.HubMasterDTO;
import com.example.service.HubService;
import org.springframework.http.ResponseEntity; // Add this import
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class HubController {

    private final HubService hubService;

    public HubController(HubService hubService) {
        this.hubService = hubService;
    }

    // UPDATE: Added /city/ to match your React fetch call
    @GetMapping("/hubs/city/{cityId}")
    public ResponseEntity<List<HubMasterDTO>> getHubsByCity(@PathVariable Long cityId) {
        List<HubMasterDTO> hubs = hubService.getHubsByCity(cityId);
        return ResponseEntity.ok(hubs);
    }
}