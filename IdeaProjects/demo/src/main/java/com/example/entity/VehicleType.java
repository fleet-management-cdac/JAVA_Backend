package com.example.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "vehicle_types")
public class VehicleType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_type_id", nullable = false)
    private Long id;

    @Column(name = "type_name", nullable = false, length = 100)
    private String typeName;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "img_url", length = 100)
    private String imgUrl;

    @OneToMany(mappedBy = "vehicleType")
    private Set<VehicleRate> vehicleRates = new LinkedHashSet<>();

    @OneToMany(mappedBy = "vehicleType")
    private Set<Vehicle> vehicles = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Set<VehicleRate> getVehicleRates() {
        return vehicleRates;
    }

    public void setVehicleRates(Set<VehicleRate> vehicleRates) {
        this.vehicleRates = vehicleRates;
    }

    public Set<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

}