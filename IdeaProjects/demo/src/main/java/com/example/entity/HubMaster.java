package com.example.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "hub_master")
public class HubMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hub_id", nullable = false)
    private Long id;

    @Column(name = "hub_name", length = 30)
    private String hubName;

    @Column(name = "hub_address", length = 100)
    private String hubAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private CityMaster city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private StateMaster state;

    @OneToMany(mappedBy = "hub")
    private Set<AirportMaster> airportMasters = new LinkedHashSet<>();

    @OneToMany(mappedBy = "pickupHub")
    private Set<Booking> bookings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "hub")
    private Set<Vehicle> vehicles = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHubName() {
        return hubName;
    }

    public void setHubName(String hubName) {
        this.hubName = hubName;
    }

    public String getHubAddress() {
        return hubAddress;
    }

    public void setHubAddress(String hubAddress) {
        this.hubAddress = hubAddress;
    }

    public CityMaster getCity() {
        return city;
    }

    public void setCity(CityMaster city) {
        this.city = city;
    }

    public StateMaster getState() {
        return state;
    }

    public void setState(StateMaster state) {
        this.state = state;
    }

    public Set<AirportMaster> getAirportMasters() {
        return airportMasters;
    }

    public void setAirportMasters(Set<AirportMaster> airportMasters) {
        this.airportMasters = airportMasters;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public Set<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

}