package com.example.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvoiceDTO {
    private Long invoiceId;
    private LocalDate invoiceDate;
    private Long bookingId;
    private String customerName;
    private LocalDate handoverDate;
    private LocalDate returnDate;
    private BigDecimal rentalAmount;
    private BigDecimal addonTotalAmount;
    private BigDecimal totalAmount;

    // 1. Default Constructor (Required)
    public InvoiceDTO() {}

    // 2. All-Arguments Constructor (Fixes the "Expected 0 but found 9" error)
    public InvoiceDTO(Long invoiceId, LocalDate invoiceDate, Long bookingId, String customerName,
                      LocalDate handoverDate, LocalDate returnDate, BigDecimal rentalAmount,
                      BigDecimal addonTotalAmount, BigDecimal totalAmount) {
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.handoverDate = handoverDate;
        this.returnDate = returnDate;
        this.rentalAmount = rentalAmount;
        this.addonTotalAmount = addonTotalAmount;
        this.totalAmount = totalAmount;
    }

    // 3. GETTERS (Fixes the "Cannot resolve method" errors)
    public Long getInvoiceId() { return invoiceId; }
    public LocalDate getInvoiceDate() { return invoiceDate; }
    public Long getBookingId() { return bookingId; }
    public String getCustomerName() { return customerName; }
    public LocalDate getHandoverDate() { return handoverDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public BigDecimal getRentalAmount() { return rentalAmount; }
    public BigDecimal getAddonTotalAmount() { return addonTotalAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }

    // 4. SETTERS (Required for POST requests to work)
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setHandoverDate(LocalDate handoverDate) { this.handoverDate = handoverDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setRentalAmount(BigDecimal rentalAmount) { this.rentalAmount = rentalAmount; }
    public void setAddonTotalAmount(BigDecimal addonTotalAmount) { this.addonTotalAmount = addonTotalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}