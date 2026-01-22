package com.example.service;

import com.example.dto.CityMasterDTO;
import com.example.entity.CityMaster;
import com.example.repository.CityMasterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityMasterService {

    private final CityMasterRepository cityMasterRepository;

    // Constructor Injection
    public CityMasterService(CityMasterRepository cityMasterRepository) {
        this.cityMasterRepository = cityMasterRepository;
    }

    // 1. Get all cities for a specific state
    public List<CityMasterDTO> getCitiesByState(Long stateId) {
        return cityMasterRepository.findByState_Id(stateId)
                .stream()
                .map(city -> new CityMasterDTO(city.getId(), city.getCityName()))
                .collect(Collectors.toList());
    }

    // 2. Get a specific city by its ID
    public CityMasterDTO getCityById(Long cityId) {
        CityMaster city = cityMasterRepository.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City not found with ID: " + cityId));
        return new CityMasterDTO(city.getId(), city.getCityName());
    }
}