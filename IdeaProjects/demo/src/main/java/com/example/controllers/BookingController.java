package com.example.controllers;

import com.example.dto.ApiResponseDTO;
import com.example.dto.BookingRequestDTO;
import com.example.dto.BookingResponseDTO;
import com.example.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // CREATE - Push to both tables
    @PostMapping
    public ResponseEntity<ApiResponseDTO<BookingResponseDTO>> createBooking(
            @RequestBody BookingRequestDTO request) {

        ApiResponseDTO<BookingResponseDTO> response = bookingService.createBooking(request);

        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // READ - Get by ID
    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponseDTO<BookingResponseDTO>> getBookingById(
            @PathVariable Long bookingId) {

        ApiResponseDTO<BookingResponseDTO> response = bookingService.getBookingById(bookingId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // READ - Get all
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<BookingResponseDTO>>> getAllBookings() {
        ApiResponseDTO<List<BookingResponseDTO>> response = bookingService.getAllBookings();
        return ResponseEntity.ok(response);
    }

    // READ - Get by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDTO<List<BookingResponseDTO>>> getBookingsByUserId(
            @PathVariable Long userId) {

        ApiResponseDTO<List<BookingResponseDTO>> response = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    // UPDATE - Status only
    @PatchMapping("/{bookingId}/status")
    public ResponseEntity<ApiResponseDTO<BookingResponseDTO>> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam String status) {

        ApiResponseDTO<BookingResponseDTO> response = bookingService.updateBookingStatus(bookingId, status);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // DELETE
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteBooking(@PathVariable Long bookingId) {

        ApiResponseDTO<Void> response = bookingService.deleteBooking(bookingId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}