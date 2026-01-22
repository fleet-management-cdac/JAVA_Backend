
package com.example.controllers;

import com.example.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // Test this in Postman: POST http://localhost:8080/api/email/test-confirmation?email=your@email.com
    @PostMapping("/test-confirmation")
    public String testEmail(@RequestParam String email) {
        try {
            emailService.sendBookingConfirmation(email, "Test Customer", 12345L);
            return "Email sent successfully to " + email;
        } catch (Exception e) {
            return "Error sending email: " + e.getMessage();
        }
    }

    @PostMapping("/test-invoice")
    public String testInvoice(@RequestParam String email) {
        // Simulating the PDF byte array that your InvoiceService will create
        byte[] simulatedPdf = "SIMULATED PDF INVOICE DATA".getBytes();

        emailService.sendInvoiceWithAttachment(email, "Customer Name", 12345L, simulatedPdf);
        return "Invoice PDF sent to " + email;
    }
}