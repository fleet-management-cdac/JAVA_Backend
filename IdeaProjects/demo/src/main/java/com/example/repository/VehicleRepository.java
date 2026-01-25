package com.example.repository;

import com.example.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // 1. GLOBAL count (for when no Hub is selected)
    long countByVehicleTypeIdAndStatus(Long typeId, String status);

    // 2. HUB-SPECIFIC count (for when a Hub IS selected)
    long countByVehicleTypeIdAndStatusAndHubId(Long typeId, String status, Long hubId);

    // 3. Get available vehicles by status
    List<Vehicle> findByStatus(String status);

    // 4. Get available vehicles by status and hub
    List<Vehicle> findByStatusAndHubId(String status, Long hubId);

    // 5. Get available vehicles by status, hub, and vehicle type
    List<Vehicle> findByStatusAndHubIdAndVehicleTypeId(String status, Long hubId, Long vehicleTypeId);

    // 6. Get available vehicles by status and vehicle type (no hub filter)
    List<Vehicle> findByStatusAndVehicleTypeId(String status, Long vehicleTypeId);
}