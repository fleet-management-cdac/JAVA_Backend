package com.example.controllers;

import com.example.dto.HubMasterDTO;
import com.example.service.HubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // <--- NEW IMPORT

import java.util.List;

@RestController
@RequestMapping("/api/locations") // Your Base URL is /api/locations
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

    // --- YOUR EXISTING METHOD (Keep this exactly as is) ---
    // UPDATE: Added /city/ to match your React fetch call
    @GetMapping("/hubs/city/{cityId}")
    public ResponseEntity<List<HubMasterDTO>> getHubsByCity(@PathVariable Long cityId) {
        List<HubMasterDTO> hubs = hubService.getHubsByCity(cityId);
        return ResponseEntity.ok(hubs);
    }

    // --- NEW UPLOAD METHOD (Add this below) ---
    @PostMapping("/upload")
    public ResponseEntity<String> uploadHubs(@RequestParam("file") MultipartFile file) {
        try {
            hubService.saveHubsFromExcel(file);
            return ResponseEntity.ok("Hubs uploaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage());
        }
    }
}