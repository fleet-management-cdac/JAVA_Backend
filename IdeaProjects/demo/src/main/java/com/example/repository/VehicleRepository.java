package com.example.repository;

import com.example.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // 1. GLOBAL count (for when no Hub is selected)
    long countByVehicleTypeIdAndStatus(Long typeId, String status);

    // 2. HUB-SPECIFIC count (for when a Hub IS selected)
    long countByVehicleTypeIdAndStatusAndHubId(Long typeId, String status, Long hubId);
}