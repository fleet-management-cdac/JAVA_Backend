package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.InvoiceResponseDTO;
import com.example.dto.VehicleReturnRequestDTO;
import com.example.entity.*;
import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceHeaderRepository invoiceHeaderRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HandoverRepository handoverRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleRateRepository vehicleRateRepository;
    @Autowired
    private PdfInvoiceService pdfInvoiceService;
    @Autowired
    private EmailService emailService;

    // ========== PROCESS VEHICLE RETURN & GENERATE INVOICE ==========
    @Transactional
    public ApiResponseDTO<InvoiceResponseDTO> processVehicleReturn(VehicleReturnRequestDTO request) {

        // Validate
        if (request.getBookingId() == null) {
            return ApiResponseDTO.error("Booking ID is required");
        }
        if (request.getActualReturnDate() == null) {
            return ApiResponseDTO.error("Return date is required");
        }

        // Fetch booking
        Optional<Booking> bookingOpt = bookingRepository.findByIdWithDetails(request.getBookingId());
        if (bookingOpt.isEmpty()) {
            return ApiResponseDTO.error("Booking not found");
        }

        Booking booking = bookingOpt.get();

        if ("completed".equals(booking.getStatus())) {
            return ApiResponseDTO.error("Booking already completed");
        }
        if ("cancelled".equals(booking.getStatus())) {
            return ApiResponseDTO.error("Booking was cancelled");
        }

        // Check handover exists
        List<Handover> handovers = handoverRepository.findByBookingId(booking.getId());
        if (handovers.isEmpty()) {
            return ApiResponseDTO.error("No handover record found");
        }

        Handover handover = handovers.get(0);
        BookingCustomerDetail customerDetail = booking.getBookingCustomerDetail();

        if (customerDetail == null) {
            return ApiResponseDTO.error("Customer details not found");
        }

        // Get dates
        LocalDate handoverDate = handover.getCreatedAt()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate returnDate = request.getActualReturnDate();

        // Calculate rental days (minimum 1)
        long totalDays = ChronoUnit.DAYS.between(handoverDate, returnDate);
        if (totalDays < 1) {
            totalDays = 1;
        }

        // Get vehicle from handover (not from booking anymore)
        Vehicle vehicle = handover.getVehicle();
        if (vehicle == null || vehicle.getVehicleType() == null) {
            return ApiResponseDTO.error("Vehicle not found in handover");
        }

        Long vehicleTypeId = vehicle.getVehicleType().getId();

        // Fetch all rates for this vehicle type
        List<VehicleRate> rates = vehicleRateRepository.findByVehicleTypeId(vehicleTypeId);

        // Create rate map
        Map<String, BigDecimal> rateMap = new HashMap<>();
        for (VehicleRate rate : rates) {
            rateMap.put(rate.getPlans().toLowerCase(), rate.getAmount());
        }

        BigDecimal dailyRate = rateMap.getOrDefault("daily", BigDecimal.ZERO);
        BigDecimal weeklyRate = rateMap.getOrDefault("weekly", BigDecimal.ZERO);
        BigDecimal monthlyRate = rateMap.getOrDefault("monthly", BigDecimal.ZERO);

        // ========== SMART PRICING CALCULATION ==========
        RentalCalculation calc = calculateSmartRental(totalDays, dailyRate, weeklyRate, monthlyRate);

        // Calculate addon amount
        BigDecimal addonTotalAmount = BigDecimal.ZERO;
        Addon addon = booking.getAddon();
        if (addon != null && addon.getPricePerDay() != null) {
            addonTotalAmount = addon.getPricePerDay().multiply(BigDecimal.valueOf(totalDays));
        }

        // Total amount
        BigDecimal totalAmount = calc.totalRentalAmount.add(addonTotalAmount);

        // Create Invoice
        InvoiceHeader invoice = new InvoiceHeader();
        invoice.setDate(LocalDate.now());
        invoice.setBookingCustomer(customerDetail);
        invoice.setHandoverDate(handoverDate);
        invoice.setReturnDate(returnDate);
        invoice.setRentalAmount(calc.totalRentalAmount);
        invoice.setAddonTotalAmount(addonTotalAmount);
        invoice.setTotalAmount(totalAmount);

        invoice = invoiceHeaderRepository.save(invoice);

        // Update booking status
        booking.setStatus("completed");
        bookingRepository.save(booking);

        // Update vehicle status
        vehicle.setStatus("available");
        vehicleRepository.save(vehicle);

        // Build detailed response
        InvoiceResponseDTO response = new InvoiceResponseDTO();
        response.setInvoiceId(invoice.getId());
        response.setInvoiceDate(invoice.getDate());
        response.setBookingId(booking.getId());
        response.setBookingCustomerId(customerDetail.getId());
        response.setCustomerName(customerDetail.getFirstName() + " " +
                (customerDetail.getLastName() != null ? customerDetail.getLastName() : ""));
        response.setCustomerEmail(customerDetail.getEmail());
        response.setVehicleName(vehicle.getCompany() + " " + vehicle.getModel());
        response.setVehicleRegistration(vehicle.getRegistrationNo());
        response.setHandoverDate(handoverDate);
        response.setReturnDate(returnDate);
        response.setTotalDays(totalDays);

        // Pricing breakdown
        response.setDailyRate(dailyRate);
        response.setWeeklyRate(weeklyRate);
        response.setMonthlyRate(monthlyRate);
        response.setMonthsCharged(calc.months);
        response.setWeeksCharged(calc.weeks);
        response.setDaysCharged(calc.days);
        response.setMonthlyAmount(calc.monthlyAmount);
        response.setWeeklyAmount(calc.weeklyAmount);
        response.setDailyAmount(calc.dailyAmount);
        response.setRentalAmount(calc.totalRentalAmount);

        if (addon != null) {
            response.setAddonName(addon.getName());
            response.setAddonPricePerDay(addon.getPricePerDay());
        }
        response.setAddonTotalAmount(addonTotalAmount);
        response.setTotalAmount(totalAmount);

        // Breakdown string for display
        response.setPricingBreakdown(calc.breakdownString);

        try {
            byte[] pdfBytes = pdfInvoiceService.generateInvoicePdf(response);

            emailService.sendInvoiceWithAttachment(
                    customerDetail.getEmail(),
                    response.getCustomerName(),
                    booking.getId(),
                    pdfBytes);

            System.out.println("✅ Invoice PDF sent to: " + customerDetail.getEmail());
        } catch (Exception e) {
            System.err.println("⚠️ Failed to send invoice email: " + e.getMessage());
        }
        return ApiResponseDTO.success("Vehicle returned & invoice generated", response);

    }

    // ========== SMART RENTAL CALCULATION ==========
    private RentalCalculation calculateSmartRental(long totalDays,
            BigDecimal dailyRate, BigDecimal weeklyRate, BigDecimal monthlyRate) {

        RentalCalculation calc = new RentalCalculation();
        long remainingDays = totalDays;
        StringBuilder breakdown = new StringBuilder();

        // Calculate months (30 days each)
        if (remainingDays >= 30 && monthlyRate.compareTo(BigDecimal.ZERO) > 0) {
            calc.months = remainingDays / 30;
            remainingDays = remainingDays % 30;
            calc.monthlyAmount = monthlyRate.multiply(BigDecimal.valueOf(calc.months));
            breakdown.append(calc.months).append(" month(s) × ₹").append(monthlyRate).append(" = ₹")
                    .append(calc.monthlyAmount);
        }

        // Calculate weeks (7 days each)
        if (remainingDays >= 7 && weeklyRate.compareTo(BigDecimal.ZERO) > 0) {
            calc.weeks = remainingDays / 7;
            remainingDays = remainingDays % 7;
            calc.weeklyAmount = weeklyRate.multiply(BigDecimal.valueOf(calc.weeks));
            if (breakdown.length() > 0)
                breakdown.append(" + ");
            breakdown.append(calc.weeks).append(" week(s) × ₹").append(weeklyRate).append(" = ₹")
                    .append(calc.weeklyAmount);
        }

        // Remaining days
        if (remainingDays > 0 && dailyRate.compareTo(BigDecimal.ZERO) > 0) {
            calc.days = remainingDays;
            calc.dailyAmount = dailyRate.multiply(BigDecimal.valueOf(calc.days));
            if (breakdown.length() > 0)
                breakdown.append(" + ");
            breakdown.append(calc.days).append(" day(s) × ₹").append(dailyRate).append(" = ₹").append(calc.dailyAmount);
        }

        // Total
        calc.totalRentalAmount = calc.monthlyAmount.add(calc.weeklyAmount).add(calc.dailyAmount);
        calc.breakdownString = breakdown.toString();

        return calc;
    }

    // Helper class for calculation
    private static class RentalCalculation {
        long months = 0;
        long weeks = 0;
        long days = 0;
        BigDecimal monthlyAmount = BigDecimal.ZERO;
        BigDecimal weeklyAmount = BigDecimal.ZERO;
        BigDecimal dailyAmount = BigDecimal.ZERO;
        BigDecimal totalRentalAmount = BigDecimal.ZERO;
        String breakdownString = "";
    }

    // ========== GET INVOICE BY ID ==========
    public ApiResponseDTO<InvoiceResponseDTO> getInvoiceById(Long invoiceId) {
        Optional<InvoiceHeader> invoiceOpt = invoiceHeaderRepository.findByIdWithDetails(invoiceId);

        if (invoiceOpt.isEmpty()) {
            return ApiResponseDTO.error("Invoice not found");
        }

        InvoiceHeader invoice = invoiceOpt.get();
        return ApiResponseDTO.success("Invoice fetched", buildInvoiceResponse(invoice));
    }

    // ========== GET INVOICE BY BOOKING ID ==========
    public ApiResponseDTO<InvoiceResponseDTO> getInvoiceByBookingId(Long bookingId) {
        Optional<InvoiceHeader> invoiceOpt = invoiceHeaderRepository.findByBookingId(bookingId);

        if (invoiceOpt.isEmpty()) {
            return ApiResponseDTO.error("Invoice not found for this booking");
        }

        InvoiceHeader invoice = invoiceOpt.get();
        return ApiResponseDTO.success("Invoice fetched", buildInvoiceResponse(invoice));
    }

    // ========== Helper ==========
    private InvoiceResponseDTO buildInvoiceResponse(InvoiceHeader invoice) {
        InvoiceResponseDTO response = new InvoiceResponseDTO();
        response.setInvoiceId(invoice.getId());
        response.setInvoiceDate(invoice.getDate());
        response.setHandoverDate(invoice.getHandoverDate());
        response.setReturnDate(invoice.getReturnDate());
        response.setRentalAmount(invoice.getRentalAmount());
        response.setAddonTotalAmount(invoice.getAddonTotalAmount());
        response.setTotalAmount(invoice.getTotalAmount());

        if (invoice.getBookingCustomer() != null) {
            BookingCustomerDetail cd = invoice.getBookingCustomer();
            response.setBookingCustomerId(cd.getId());
            response.setCustomerName(cd.getFirstName() + " " +
                    (cd.getLastName() != null ? cd.getLastName() : ""));
            response.setCustomerEmail(cd.getEmail());

            if (cd.getBooking() != null) {
                Booking booking = cd.getBooking();
                response.setBookingId(booking.getId());

                // Get vehicle from handover
                List<Handover> handovers = handoverRepository.findByBookingId(booking.getId());
                if (!handovers.isEmpty() && handovers.get(0).getVehicle() != null) {
                    Vehicle vehicle = handovers.get(0).getVehicle();
                    response.setVehicleName(vehicle.getCompany() + " " + vehicle.getModel());
                    response.setVehicleRegistration(vehicle.getRegistrationNo());
                }
            }
        }

        return response;
    }
}