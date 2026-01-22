package com.example.controllers;

import com.example.dto.InvoiceDTO;
import com.example.service.InvoiceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*") // Allows your React/Frontend to connect
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    /**
     * STEP 1: Logical Save
     * This saves the invoice record and updates the Vehicle to 'available'.
     */
    @PostMapping("/save")
    public ResponseEntity<InvoiceDTO> createInvoice(@RequestBody InvoiceDTO dto) {
        return new ResponseEntity<>(invoiceService.saveInvoice(dto), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<InvoiceDTO>> fetchInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    /**
     * STEP 2: PDF Generation (GET Method)
     * This fetches data from the database using bookingId and converts it to PDF.
     */
    @GetMapping("/download/{bookingId}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long bookingId) {
        try {
            // Generate the PDF bytes from the service
            byte[] pdfContent = invoiceService.generateInvoicePdf(bookingId);

            HttpHeaders headers = new HttpHeaders();

            // Set content type to PDF so the browser knows how to render it
            headers.setContentType(MediaType.APPLICATION_PDF);

            // Use "inline" to open in browser tab, or "attachment" to download directly
            headers.setContentDispositionFormData("inline", "Invoice_Booking_" + bookingId + ".pdf");

            // Cache control to prevent issues with stale PDF data
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);

        } catch (RuntimeException e) {
            // If the invoice wasn't saved yet or bookingId is wrong
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}