package com.example.repository;

import com.example.entity.AirportMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<AirportMaster, Long> {
}