package com.example.controllers;

import com.example.dto.ApiResponseDTO;
import com.example.dto.AssignHubRequestDTO;
import com.example.dto.CreateStaffRequestDTO;
import com.example.dto.StaffResponseDTO;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin Controller - Restricted to users with 'admin' role only
 * Manages staff users: create, list, assign hubs, delete
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAuthority('ROLE_ADMIN')") // All endpoints require admin role
public class AdminController {

    @Autowired
    private UserService userService;

    /**
     * Create a new staff member
     * POST /api/admin/staff
     */
    @PostMapping("/staff")
    public ResponseEntity<ApiResponseDTO<StaffResponseDTO>> createStaff(@RequestBody CreateStaffRequestDTO request) {
        ApiResponseDTO<StaffResponseDTO> response = userService.createStaff(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all staff members with hub info
     * GET /api/admin/staff
     */
    @GetMapping("/staff")
    public ResponseEntity<ApiResponseDTO<List<StaffResponseDTO>>> getAllStaff() {
        ApiResponseDTO<List<StaffResponseDTO>> response = userService.getAllStaff();
        return ResponseEntity.ok(response);
    }

    /**
     * Assign or change hub for a staff member
     * PUT /api/admin/staff/{userId}/hub
     */
    @PutMapping("/staff/{userId}/hub")
    public ResponseEntity<ApiResponseDTO<String>> assignHub(
            @PathVariable Long userId,
            @RequestBody AssignHubRequestDTO request) {
        ApiResponseDTO<String> response = userService.assignHubToUser(userId, request.getHubId());
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a staff member
     * DELETE /api/admin/staff/{userId}
     */
    @DeleteMapping("/staff/{userId}")
    public ResponseEntity<ApiResponseDTO<String>> deleteStaff(@PathVariable Long userId) {
        ApiResponseDTO<String> response = userService.deleteStaff(userId);
        return ResponseEntity.ok(response);
    }
}
