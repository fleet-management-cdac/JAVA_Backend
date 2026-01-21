package com.example.repository;

import com.example.entity.HubMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HubRepository extends JpaRepository<HubMaster, Long> {
    // This matches the 'city_id' column through the City relationship
    List<HubMaster> findByCity_Id(Long cityId);
}