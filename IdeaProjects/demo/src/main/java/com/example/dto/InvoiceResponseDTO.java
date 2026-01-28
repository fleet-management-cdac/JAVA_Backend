package com.example.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvoiceResponseDTO {
    private Long invoiceId;
    private LocalDate invoiceDate;
    private Long bookingId;
    private Long bookingCustomerId;
    private String customerName;
    private String customerEmail;
    private String vehicleName;
    private String vehicleRegistration;
    private LocalDate handoverDate;
    private LocalDate returnDate;
    private Long totalDays;

    // Rate information
    private BigDecimal dailyRate;
    private BigDecimal weeklyRate;
    private BigDecimal monthlyRate;

    // Pricing breakdown
    private Long monthsCharged;
    private Long weeksCharged;
    private Long daysCharged;
    private BigDecimal monthlyAmount;
    private BigDecimal weeklyAmount;
    private BigDecimal dailyAmount;
    private String pricingBreakdown;  // Human readable breakdown

    private BigDecimal rentalAmount;

    // Addon
    private String addonName;
    private BigDecimal addonPricePerDay;
    private BigDecimal addonTotalAmount;

    private BigDecimal totalAmount;

    // Getters and Setters
    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }

    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Long getBookingCustomerId() { return bookingCustomerId; }
    public void setBookingCustomerId(Long bookingCustomerId) { this.bookingCustomerId = bookingCustomerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getVehicleName() { return vehicleName; }
    public void setVehicleName(String vehicleName) { this.vehicleName = vehicleName; }

    public String getVehicleRegistration() { return vehicleRegistration; }
    public void setVehicleRegistration(String vehicleRegistration) { this.vehicleRegistration = vehicleRegistration; }

    public LocalDate getHandoverDate() { return handoverDate; }
    public void setHandoverDate(LocalDate handoverDate) { this.handoverDate = handoverDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public Long getTotalDays() { return totalDays; }
    public void setTotalDays(Long totalDays) { this.totalDays = totalDays; }

    public BigDecimal getDailyRate() { return dailyRate; }
    public void setDailyRate(BigDecimal dailyRate) { this.dailyRate = dailyRate; }

    public BigDecimal getWeeklyRate() { return weeklyRate; }
    public void setWeeklyRate(BigDecimal weeklyRate) { this.weeklyRate = weeklyRate; }

    public BigDecimal getMonthlyRate() { return monthlyRate; }
    public void setMonthlyRate(BigDecimal monthlyRate) { this.monthlyRate = monthlyRate; }

    public Long getMonthsCharged() { return monthsCharged; }
    public void setMonthsCharged(Long monthsCharged) { this.monthsCharged = monthsCharged; }

    public Long getWeeksCharged() { return weeksCharged; }
    public void setWeeksCharged(Long weeksCharged) { this.weeksCharged = weeksCharged; }

    public Long getDaysCharged() { return daysCharged; }
    public void setDaysCharged(Long daysCharged) { this.daysCharged = daysCharged; }

    public BigDecimal getMonthlyAmount() { return monthlyAmount; }
    public void setMonthlyAmount(BigDecimal monthlyAmount) { this.monthlyAmount = monthlyAmount; }

    public BigDecimal getWeeklyAmount() { return weeklyAmount; }
    public void setWeeklyAmount(BigDecimal weeklyAmount) { this.weeklyAmount = weeklyAmount; }

    public BigDecimal getDailyAmount() { return dailyAmount; }
    public void setDailyAmount(BigDecimal dailyAmount) { this.dailyAmount = dailyAmount; }

    public String getPricingBreakdown() { return pricingBreakdown; }
    public void setPricingBreakdown(String pricingBreakdown) { this.pricingBreakdown = pricingBreakdown; }

    public BigDecimal getRentalAmount() { return rentalAmount; }
    public void setRentalAmount(BigDecimal rentalAmount) { this.rentalAmount = rentalAmount; }

    public String getAddonName() { return addonName; }
    public void setAddonName(String addonName) { this.addonName = addonName; }

    public BigDecimal getAddonPricePerDay() { return addonPricePerDay; }
    public void setAddonPricePerDay(BigDecimal addonPricePerDay) { this.addonPricePerDay = addonPricePerDay; }

    public BigDecimal getAddonTotalAmount() { return addonTotalAmount; }
    public void setAddonTotalAmount(BigDecimal addonTotalAmount) { this.addonTotalAmount = addonTotalAmount; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    // === NEW FIELDS ===
    private String offerName;
    private BigDecimal discountAmount;

    // Getters and Setters
    public String getOfferName() { return offerName; }
    public void setOfferName(String offerName) { this.offerName = offerName; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
}