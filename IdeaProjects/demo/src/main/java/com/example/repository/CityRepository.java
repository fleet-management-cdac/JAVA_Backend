package com.example.repository;

import com.example.entity.CityMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<CityMaster, Long> {

    List<CityMaster> findByState_Id(Long stateId);
}