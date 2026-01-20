package com.example.repository;

import com.example.entity.VehicleRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRateRepository extends JpaRepository<VehicleRate, Long> {
    
    // Custom query to find rates for a specific vehicle type
    List<VehicleRate> findByVehicleTypeId(Long vehicleTypeId);
}