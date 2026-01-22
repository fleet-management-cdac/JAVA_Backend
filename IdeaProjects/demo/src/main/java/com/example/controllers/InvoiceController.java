package com.example.controllers;

import com.example.dto.ApiResponseDTO;
import com.example.dto.InvoiceResponseDTO;
import com.example.dto.VehicleReturnRequestDTO;
import com.example.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    // PROCESS VEHICLE RETURN & GENERATE INVOICE
    @PostMapping("/return")
    public ResponseEntity<ApiResponseDTO<InvoiceResponseDTO>> processVehicleReturn(
            @RequestBody VehicleReturnRequestDTO request) {

        ApiResponseDTO<InvoiceResponseDTO> response = invoiceService.processVehicleReturn(request);

        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // GET INVOICE BY ID
    @GetMapping("/{invoiceId}")
    public ResponseEntity<ApiResponseDTO<InvoiceResponseDTO>> getInvoiceById(
            @PathVariable Long invoiceId) {

        ApiResponseDTO<InvoiceResponseDTO> response = invoiceService.getInvoiceById(invoiceId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // GET INVOICE BY BOOKING ID
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponseDTO<InvoiceResponseDTO>> getInvoiceByBookingId(
            @PathVariable Long bookingId) {

        ApiResponseDTO<InvoiceResponseDTO> response = invoiceService.getInvoiceByBookingId(bookingId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}