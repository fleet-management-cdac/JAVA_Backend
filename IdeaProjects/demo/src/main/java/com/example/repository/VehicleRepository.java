package com.example.repository;

import com.example.entity.Vehicle;
import com.example.entity.VehicleStatus; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // Custom query to count available cars
    long countByVehicleTypeIdAndStatus(Long typeId, String status);
    List<Vehicle> findByStatus(String status);
}