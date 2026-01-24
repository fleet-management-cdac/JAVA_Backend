package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // ========== CREATE ORDER ==========
    @PostMapping("/create-order")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> createOrder(@RequestBody Map<String, Object> request) {
        Long invoiceId = Long.valueOf(request.get("invoiceId").toString());
        ApiResponseDTO<Map<String, Object>> response = paymentService.createOrder(invoiceId);
        return ResponseEntity.ok(response);
    }

    // ========== VERIFY PAYMENT ==========
    @PostMapping("/verify")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> verifyPayment(@RequestBody Map<String, Object> request) {
        String razorpayOrderId = (String) request.get("razorpayOrderId");
        String razorpayPaymentId = (String) request.get("razorpayPaymentId");
        String razorpaySignature = (String) request.get("razorpaySignature");
        Long invoiceId = Long.valueOf(request.get("invoiceId").toString());

        ApiResponseDTO<Map<String, Object>> response = paymentService.verifyPayment(
                razorpayOrderId,
                razorpayPaymentId,
                razorpaySignature,
                invoiceId);

        return ResponseEntity.ok(response);
    }
}
