package com.example.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAuth user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_type_id")
    private VehicleType vehicleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rate_id")
    private VehicleRate rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_hub_id")
    private HubMaster pickupHub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_hub_id")
    private HubMaster returnHub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addon_id")
    private Addon addon;

    @Column(name = "pickup_datetime", nullable = false)
    private Instant pickupDatetime;

    @Column(name = "return_datetime", nullable = false)
    private Instant returnDatetime;

    @ColumnDefault("'reserved'")
    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "booking")
    private Set<BookingAddon> bookingAddons = new LinkedHashSet<>();

    @OneToOne(mappedBy = "booking")
    private BookingCustomerDetail bookingCustomerDetail;

    @OneToMany(mappedBy = "booking")
    private Set<Handover> handovers = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserAuth getUser() {
        return user;
    }

    public void setUser(UserAuth user) {
        this.user = user;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public VehicleRate getRate() {
        return rate;
    }

    public void setRate(VehicleRate rate) {
        this.rate = rate;
    }

    public HubMaster getPickupHub() {
        return pickupHub;
    }

    public void setPickupHub(HubMaster pickupHub) {
        this.pickupHub = pickupHub;
    }

    public HubMaster getReturnHub() {
        return returnHub;
    }

    public void setReturnHub(HubMaster returnHub) {
        this.returnHub = returnHub;
    }

    public Instant getPickupDatetime() {
        return pickupDatetime;
    }

    public void setPickupDatetime(Instant pickupDatetime) {
        this.pickupDatetime = pickupDatetime;
    }

    public Instant getReturnDatetime() {
        return returnDatetime;
    }

    public void setReturnDatetime(Instant returnDatetime) {
        this.returnDatetime = returnDatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Set<BookingAddon> getBookingAddons() {
        return bookingAddons;
    }
    // Add after the addon field in Booking.java

    public Addon getAddon() {
        return addon;
    }

    public void setAddon(Addon addon) {
        this.addon = addon;
    }

    public void setBookingAddons(Set<BookingAddon> bookingAddons) {
        this.bookingAddons = bookingAddons;
    }

    public BookingCustomerDetail getBookingCustomerDetail() {
        return bookingCustomerDetail;
    }

    public void setBookingCustomerDetail(BookingCustomerDetail bookingCustomerDetail) {
        this.bookingCustomerDetail = bookingCustomerDetail;
    }

    public Set<Handover> getHandovers() {
        return handovers;
    }

    public void setHandovers(Set<Handover> handovers) {
        this.handovers = handovers;
    }

}