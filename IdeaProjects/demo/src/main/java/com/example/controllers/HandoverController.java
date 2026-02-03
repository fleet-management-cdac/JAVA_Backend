package com.example.controllers;

import com.example.dto.ApiResponseDTO;
import com.example.dto.HandoverRequestDTO;
import com.example.dto.HandoverResponseDTO;
import com.example.service.HandoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/handovers")
@CrossOrigin(origins = "*")
public class HandoverController {

    @Autowired
    private HandoverService handoverService;

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponseDTO<HandoverResponseDTO>> createHandover(
            @RequestBody HandoverRequestDTO request) {

        ApiResponseDTO<HandoverResponseDTO> response = handoverService.createHandover(request);

        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // GET by ID
    @GetMapping("/{handoverId}")
    public ResponseEntity<ApiResponseDTO<HandoverResponseDTO>> getHandoverById(
            @PathVariable Long handoverId) {

        ApiResponseDTO<HandoverResponseDTO> response = handoverService.getHandoverById(handoverId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // GET by Booking ID
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponseDTO<List<HandoverResponseDTO>>> getHandoversByBookingId(
            @PathVariable Long bookingId) {

        ApiResponseDTO<List<HandoverResponseDTO>> response = handoverService.getHandoversByBookingId(bookingId);
        return ResponseEntity.ok(response);
    }

    // PROCESS RETURN (Updates vehicle hub to return location)
    @PostMapping("/return")
    public ResponseEntity<ApiResponseDTO<HandoverResponseDTO>> processReturn(
            @RequestBody HandoverRequestDTO request) {

        ApiResponseDTO<HandoverResponseDTO> response = handoverService.processReturnHandover(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}