package com.example.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@Table(name = "booking_addons")
public class BookingAddon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addon_id")
    private Addon addon;

    @ColumnDefault("1")
    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "addon_total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal addonTotalAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Addon getAddon() {
        return addon;
    }

    public void setAddon(Addon addon) {
        this.addon = addon;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAddonTotalAmount() {
        return addonTotalAmount;
    }

    public void setAddonTotalAmount(BigDecimal addonTotalAmount) {
        this.addonTotalAmount = addonTotalAmount;
    }

}