package com.example.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "invoice_header")
public class InvoiceHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id", nullable = false)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_customer_id")
    private BookingCustomerDetail bookingCustomer;

    @Column(name = "handover_date")
    private LocalDate handoverDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "rental_amount", precision = 10, scale = 2)
    private BigDecimal rentalAmount;

    @Column(name = "addon_total_amount", precision = 10, scale = 2)
    private BigDecimal addonTotalAmount;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BookingCustomerDetail getBookingCustomer() {
        return bookingCustomer;
    }

    public void setBookingCustomer(BookingCustomerDetail bookingCustomer) {
        this.bookingCustomer = bookingCustomer;
    }

    public LocalDate getHandoverDate() {
        return handoverDate;
    }

    public void setHandoverDate(LocalDate handoverDate) {
        this.handoverDate = handoverDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public BigDecimal getRentalAmount() {
        return rentalAmount;
    }

    public void setRentalAmount(BigDecimal rentalAmount) {
        this.rentalAmount = rentalAmount;
    }

    public BigDecimal getAddonTotalAmount() {
        return addonTotalAmount;
    }

    public void setAddonTotalAmount(BigDecimal addonTotalAmount) {
        this.addonTotalAmount = addonTotalAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

}