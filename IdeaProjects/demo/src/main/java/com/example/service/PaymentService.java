package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.entity.InvoiceHeader;
import com.example.repository.InvoiceHeaderRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Autowired
    private InvoiceHeaderRepository invoiceHeaderRepository;

    // ========== CREATE RAZORPAY ORDER ==========
    public ApiResponseDTO<Map<String, Object>> createOrder(Long invoiceId) {
        logger.info("🚀 Creating Razorpay order for invoice ID: {}", invoiceId);

        // Fetch invoice
        Optional<InvoiceHeader> invoiceOpt = invoiceHeaderRepository.findById(invoiceId);
        if (invoiceOpt.isEmpty()) {
            logger.error("❌ Invoice not found: {}", invoiceId);
            return ApiResponseDTO.error("Invoice not found");
        }

        InvoiceHeader invoice = invoiceOpt.get();

        if ("success".equals(invoice.getPaymentStatus())) {
            logger.warn("⚠️ Invoice already paid: {}", invoiceId);
            return ApiResponseDTO.error("Invoice already paid");
        }

        try {
            RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            // Create order options
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", invoice.getTotalAmount().multiply(new java.math.BigDecimal(100)).intValue()); // Amount
            // in
            // paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "invoice_" + invoiceId);

            Order order = razorpay.orders.create(orderRequest);

            String orderId = order.get("id");
            logger.info("✅ Razorpay Order created: {}", orderId);

            // Save order ID to invoice
            invoice.setRazorpayOrderId(orderId);
            invoice.setPaymentStatus("pending");
            invoiceHeaderRepository.save(invoice);

            // Return response
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", orderId);
            response.put("amount", invoice.getTotalAmount());
            response.put("currency", "INR");
            response.put("keyId", razorpayKeyId);
            response.put("invoiceId", invoiceId);

            return ApiResponseDTO.success("Order created", response);

        } catch (RazorpayException e) {
            logger.error("❌ Razorpay error: {}", e.getMessage());
            return ApiResponseDTO.error("Payment gateway error: " + e.getMessage());
        }
    }

    // ========== VERIFY PAYMENT ==========
    public ApiResponseDTO<Map<String, Object>> verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature,
            Long invoiceId) {
        logger.info("🔐 Verifying payment for invoice: {}", invoiceId);

        // Fetch invoice
        Optional<InvoiceHeader> invoiceOpt = invoiceHeaderRepository.findById(invoiceId);
        if (invoiceOpt.isEmpty()) {
            return ApiResponseDTO.error("Invoice not found");
        }

        InvoiceHeader invoice = invoiceOpt.get();

        // Verify signature
        String generatedSignature = generateSignature(razorpayOrderId, razorpayPaymentId);

        if (!generatedSignature.equals(razorpaySignature)) {
            logger.error("❌ Signature verification failed!");
            invoice.setPaymentStatus("failed");
            invoiceHeaderRepository.save(invoice);
            return ApiResponseDTO.error("Payment verification failed");
        }

        // Success - Update invoice
        invoice.setRazorpayPaymentId(razorpayPaymentId);
        invoice.setRazorpaySignature(razorpaySignature);
        invoice.setPaymentStatus("success");
        invoiceHeaderRepository.save(invoice);

        logger.info("✅ Payment verified! Transaction ID: {}", razorpayPaymentId);

        Map<String, Object> response = new HashMap<>();
        response.put("transactionId", razorpayPaymentId);
        response.put("status", "success");
        response.put("invoiceId", invoiceId);

        return ApiResponseDTO.success("Payment successful", response);
    }

    // ========== GENERATE HMAC SIGNATURE ==========
    private String generateSignature(String orderId, String paymentId) {
        try {
            String data = orderId + "|" + paymentId;
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(razorpayKeySecret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            byte[] hash = sha256_HMAC.doFinal(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            logger.error("Error generating signature: {}", e.getMessage());
            return "";
        }
    }
}
