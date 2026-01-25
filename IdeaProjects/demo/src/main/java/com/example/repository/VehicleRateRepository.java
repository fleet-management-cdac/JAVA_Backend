package com.example.repository;

import com.example.entity.VehicleRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRateRepository extends JpaRepository<VehicleRate, Long> {
    
    // Custom query to find rates for a specific vehicle type
    @Query("SELECT vr FROM VehicleRate vr WHERE vr.vehicleType.id = :vehicleTypeId")
    List<VehicleRate> findByVehicleTypeId(@Param("vehicleTypeId") Long vehicleTypeId);
//    List<VehicleRate> findByVehicleType_VehicleTypeId(Long vehicleTypeId);
}