package com.example.repository;

import com.example.entity.HubMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HubMasterRepository extends JpaRepository<HubMaster, Long> {
    List<HubMaster> findByCity_Id(Long cityId);

    // --- ADD THIS NEW METHOD ---
    // CHANGE THIS: Return a List instead of a single object
    List<HubMaster> findByHubNameAndCity_Id(String hubName, Long cityId);

}