package com.example.repository;

import com.example.entity.HubMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HubMasterRepository extends JpaRepository<HubMaster, Long> {
}