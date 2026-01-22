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

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String FROM_EMAIL;
    /**
     * Generic method to send a simple text email
     */
    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_EMAIL); // Matches your properties file
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    /**
     * Specialized method for Booking Confirmations (to be used later)
     */
    public void sendBookingConfirmation(String toEmail, String name, Long confirmationNo) {
        String subject = "Booking Confirmed: #" + confirmationNo;
        String body = "Hello " + name + ",\n\n" +
                "Your booking has been successfully processed.\n" +
                "Confirmation Number: " + confirmationNo + "\n\n" +
                "Thank you for choosing our Fleet Service!";

        sendSimpleEmail(toEmail, subject, body);
    }

    public void sendInvoiceWithAttachment(String toEmail, String name, Long bookingId, byte[] pdfContent) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            // true = multipart message for attachments
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(FROM_EMAIL);
            helper.setTo(toEmail);
            helper.setSubject("Your Rental Invoice - Booking #" + bookingId);
            helper.setText("Hello " + name + ",\n\n" +
                    "Thank you for returning the vehicle. Please find your attached invoice.");

            // Add the PDF attachment
            helper.addAttachment("Invoice_" + bookingId + ".pdf", new ByteArrayResource(pdfContent));

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email with PDF", e);
        }
    }
}