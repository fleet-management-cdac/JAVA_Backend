package com.example.repository;

import com.example.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // Find available vehicles by hub and type
    List<Vehicle> findByHubIdAndVehicleTypeIdAndStatus(Long hubId, Long vehicleTypeId, String status);

    // Find available vehicles by hub only
    List<Vehicle> findByHubIdAndStatus(Long hubId, String status);

    // Find available vehicles by type only
    List<Vehicle> findByVehicleTypeIdAndStatus(Long vehicleTypeId, String status);

    // Find all available vehicles
    List<Vehicle> findByStatus(String status);
    // 1. GLOBAL count (for when no Hub is selected)
    long countByVehicleTypeIdAndStatus(Long typeId, String status);

    // 2. HUB-SPECIFIC count (for when a Hub IS selected)
    long countByVehicleTypeIdAndStatusAndHubId(Long typeId, String status, Long hubId);
}