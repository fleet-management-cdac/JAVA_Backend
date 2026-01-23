package com.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String FROM_EMAIL;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a").withZone(ZoneId.systemDefault());

    /**
     * Send booking confirmation with all details
     */
    public void sendBookingConfirmationEmail(
            String toEmail,
            String customerName,
            Long bookingId,
            String vehicleName,
            String vehicleRegistration,
            String pickupHub,
            String returnHub,
            Instant pickupDatetime,
            Instant returnDatetime,
            String address,
            String phoneCell
    ) {
        String subject = "🚗 Booking Confirmed - #" + bookingId + " | FLEMAN Fleet Services";

        String body = "Dear " + customerName + ",\n\n" +
                "Thank you for booking with FLEMAN Fleet Services! Your reservation has been confirmed.\n\n" +
                "═══════════════════════════════════════\n" +
                "           BOOKING DETAILS\n" +
                "═══════════════════════════════════════\n\n" +
                "📋 Booking ID: #" + bookingId + "\n\n" +
                "🚘 VEHICLE\n" +
                "   " + vehicleName + "\n" +
                "   Registration: " + vehicleRegistration + "\n\n" +
                "📅 PICKUP\n" +
                "   Date & Time: " + DATE_FORMATTER.format(pickupDatetime) + "\n" +
                "   Location: " + pickupHub + "\n\n" +
                "📅 RETURN\n" +
                "   Date & Time: " + DATE_FORMATTER.format(returnDatetime) + "\n" +
                "   Location: " + returnHub + "\n\n" +
                "═══════════════════════════════════════\n" +
                "           CUSTOMER DETAILS\n" +
                "═══════════════════════════════════════\n\n" +
                "👤 Name: " + customerName + "\n" +
                "📍 Address: " + (address != null ? address : "N/A") + "\n" +
                "📞 Phone: " + (phoneCell != null ? phoneCell : "N/A") + "\n\n" +
                "═══════════════════════════════════════\n\n" +
                "IMPORTANT REMINDERS:\n" +
                "• Please bring a valid driving license and ID proof\n" +
                "• Arrive 15 minutes before pickup time\n" +
                "• Fuel policy: Return with the same fuel level\n\n" +
                "For any queries, contact us at support@fleman.com\n\n" +
                "Thank you for choosing FLEMAN!\n" +
                "Safe Travels! 🚗\n\n" +
                "---\n" +
                "FLEMAN Fleet Management Services\n" +
                "www.fleman.com";

        sendSimpleEmail(toEmail, subject, body);
    }

    /**
     * Generic method to send a simple text email
     */
    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_EMAIL);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    /**
     * NEW: Dedicated method for Password Reset
     * This keeps the template text out of your AuthService
     */
    public void sendResetPasswordEmail(String toEmail, String resetLink) {
        String subject = "🔑 Reset Your Password - FLEMAN Fleet Services";

        String body = "Hello,\n\n" +
                "We received a request to reset your password for your FLEMAN account.\n" +
                "Please click the link below to verify your identity and set a new password:\n\n" +
                resetLink + "\n\n" +
                "⏳ This link expires in 15 minutes.\n\n" +
                "If you did not request this change, please ignore this email. Your account remains secure.\n\n" +
                "Best Regards,\n" +
                "FLEMAN Fleet Services Support";

        sendSimpleEmail(toEmail, subject, body);
    }

    /**
     * Simple booking confirmation (existing method - keep for backward compatibility)
     */
    public void sendBookingConfirmation(String toEmail, String name, Long confirmationNo) {
        String subject = "Booking Confirmed: #" + confirmationNo;
        String body = "Hello " + name + ",\n\n" +
                "Your booking has been successfully processed.\n" +
                "Confirmation Number: " + confirmationNo + "\n\n" +
                "Thank you for choosing our Fleet Service!";

        sendSimpleEmail(toEmail, subject, body);
    }

    /**
     * Send invoice with PDF attachment
     */
    public void sendInvoiceWithAttachment(String toEmail, String name, Long bookingId, byte[] pdfContent) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(FROM_EMAIL);
            helper.setTo(toEmail);
            helper.setSubject("Your Rental Invoice - Booking #" + bookingId);
            helper.setText("Hello " + name + ",\n\n" +
                    "Thank you for returning the vehicle. Please find your attached invoice.");

            helper.addAttachment("Invoice_" + bookingId + ".pdf", new ByteArrayResource(pdfContent));

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email with PDF", e);
        }
    }
}