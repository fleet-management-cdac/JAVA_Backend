package com.example.controllers;

import com.example.entity.Addon;
import com.example.service.AddonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/addons")
public class AddonController {

    @Autowired
    private AddonService addonService;

    // GET - Fetch all addons
    // URL: GET http://localhost:8080/api/addons
    @GetMapping
    public ResponseEntity<List<Addon>> getAllAddons() {
        List<Addon> addons = addonService.getAllAddons();
        return ResponseEntity.ok(addons);
    }

    // GET - Fetch addon by ID
    // URL: GET http://localhost:8080/api/addons/1
    @GetMapping("/{id}")
    public ResponseEntity<Addon> getAddonById(@PathVariable Long id) {
        return addonService.getAddonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}