package com.example.repository;

import com.example.entity.AirportMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AirportMasterRepository extends JpaRepository<AirportMaster, Long> {

    // Find all airports in a particular state
    List<AirportMaster> findByState_Id(Long stateId);

    // Find a specific airport by its code (BOM, DEL, etc.)
    Optional<AirportMaster> findByAirportCode(String airportCode);
}