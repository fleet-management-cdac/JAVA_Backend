package com.example.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "state_master")
public class StateMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "state_id", nullable = false)
    private Long id;

    @Column(name = "state_name", length = 50)
    private String stateName;

    @OneToMany(mappedBy = "state")
    private Set<AirportMaster> airportMasters = new LinkedHashSet<>();

    @OneToMany(mappedBy = "state")
    private Set<CityMaster> cityMasters = new LinkedHashSet<>();

    @OneToMany(mappedBy = "state")
    private Set<HubMaster> hubMasters = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Set<AirportMaster> getAirportMasters() {
        return airportMasters;
    }

    public void setAirportMasters(Set<AirportMaster> airportMasters) {
        this.airportMasters = airportMasters;
    }

    public Set<CityMaster> getCityMasters() {
        return cityMasters;
    }

    public void setCityMasters(Set<CityMaster> cityMasters) {
        this.cityMasters = cityMasters;
    }

    public Set<HubMaster> getHubMasters() {
        return hubMasters;
    }

    public void setHubMasters(Set<HubMaster> hubMasters) {
        this.hubMasters = hubMasters;
    }

}