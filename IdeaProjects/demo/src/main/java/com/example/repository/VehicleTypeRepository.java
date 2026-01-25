package com.example.repository;

import com.example.entity.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {

    @Query("SELECT DISTINCT vt FROM VehicleType vt LEFT JOIN FETCH vt.vehicleRates")
    List<VehicleType> findAllWithRates();
}
