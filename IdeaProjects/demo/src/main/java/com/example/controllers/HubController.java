package com.example.controllers;

import com.example.dto.HubDto;
import com.example.service.HubService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hubs")
@CrossOrigin(origins = "http://localhost:3000")
public class HubController {

    private final HubService hubService;

    public HubController(HubService hubService) {
        this.hubService = hubService;
    }

    @GetMapping
    public List<HubDto> getAllHubs() {
        return hubService.getAllHubs();
    }

    @GetMapping("/by-city/{cityId}")
    public List<HubDto> getHubsByCity(@PathVariable Long cityId) {
        return hubService.getHubsByCity(cityId);
    }
}