package com.example.repository;

import com.example.entity.HubMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HubRepository extends JpaRepository<HubMaster, Long> {

    // HubMaster.city.id
    List<HubMaster> findByCity_Id(Long cityId);
}