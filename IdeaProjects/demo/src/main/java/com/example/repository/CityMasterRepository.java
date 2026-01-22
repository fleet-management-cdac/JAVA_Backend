package com.example.repository;

import com.example.entity.CityMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CityMasterRepository extends JpaRepository<CityMaster, Long> {
    // This looks into the StateMaster object and finds the 'id' field
    List<CityMaster> findByState_Id(Long stateId);
}