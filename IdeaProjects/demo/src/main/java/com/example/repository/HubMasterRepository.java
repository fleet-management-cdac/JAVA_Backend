package com.example.repository;

import com.example.entity.HubMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HubMasterRepository extends JpaRepository<HubMaster, Long> {
    List<HubMaster> findByCity_Id(Long cityId);
}