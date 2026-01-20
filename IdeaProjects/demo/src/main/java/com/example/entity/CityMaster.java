package com.example.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "city_master")
public class CityMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id", nullable = false)
    private Long id;

    @Column(name = "city_name", length = 50)
    private String cityName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private StateMaster state;

    @OneToMany(mappedBy = "city")
    private Set<AirportMaster> airportMasters = new LinkedHashSet<>();

    @OneToMany(mappedBy = "city")
    private Set<BookingCustomerDetail> bookingCustomerDetails = new LinkedHashSet<>();

    @OneToMany(mappedBy = "city")
    private Set<HubMaster> hubMasters = new LinkedHashSet<>();

    @OneToMany(mappedBy = "city")
    private Set<UserDetail> userDetails = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public Set<BookingCustomerDetail> getBookingCustomerDetails() {
        return bookingCustomerDetails;
    }

    public void setBookingCustomerDetails(Set<BookingCustomerDetail> bookingCustomerDetails) {
        this.bookingCustomerDetails = bookingCustomerDetails;
    }

    public Set<HubMaster> getHubMasters() {
        return hubMasters;
    }

    public void setHubMasters(Set<HubMaster> hubMasters) {
        this.hubMasters = hubMasters;
    }

    public Set<UserDetail> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(Set<UserDetail> userDetails) {
        this.userDetails = userDetails;
    }

}