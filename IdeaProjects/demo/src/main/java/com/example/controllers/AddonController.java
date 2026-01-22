package com.example.controllers;



import com.example.dto.AddonResponseDTO;
import com.example.dto.ApiResponseDTO;
import com.example.service.AddonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addons")
@CrossOrigin(origins = "*")
public class AddonController {

    @Autowired
    private AddonService addonService;

    // GET ALL ADDONS
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<AddonResponseDTO>>> getAllAddons() {
        ApiResponseDTO<List<AddonResponseDTO>> response = addonService.getAllAddons();
        return ResponseEntity.ok(response);
    }

    // GET ADDON BY ID
    @GetMapping("/{addonId}")
    public ResponseEntity<ApiResponseDTO<AddonResponseDTO>> getAddonById(
            @PathVariable Long addonId) {

        ApiResponseDTO<AddonResponseDTO> response = addonService.getAddonById(addonId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(response);
        }
    }
}