package com.example.controllers;

import com.example.dto.ApiResponseDTO;
import com.example.dto.UserProfileDTO;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/details/{userDetailId}")
    public ResponseEntity<ApiResponseDTO<UserProfileDTO>> getUserDetailsById(
            @PathVariable Long userDetailId) {

        ApiResponseDTO<UserProfileDTO> response = userService.getUserDetailsById(userDetailId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(response);
        }
    }
}