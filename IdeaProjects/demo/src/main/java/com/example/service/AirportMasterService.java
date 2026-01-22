package com.example.service;

import com.example.dto.AirportMasterDTO;
import com.example.entity.AirportMaster;
import com.example.repository.AirportMasterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirportMasterService {

    private final AirportMasterRepository airportMasterRepository;

    public AirportMasterService(AirportMasterRepository airportMasterRepository) {
        this.airportMasterRepository = airportMasterRepository;
    }

    // 1. Get All Airports
    public List<AirportMasterDTO> getAllAirports() {
        return airportMasterRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 2. Get Airports by State ID
    public List<AirportMasterDTO> getAirportsByState(Long stateId) {
        return airportMasterRepository.findByState_Id(stateId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 3. Get Airport by Code (BOM, PNQ, etc.)
    public AirportMasterDTO getAirportByCode(String code) {
        AirportMaster airport = airportMasterRepository.findByAirportCode(code)
                .orElseThrow(() -> new RuntimeException("Airport code not found: " + code));
        return convertToDTO(airport);
    }

    private AirportMasterDTO convertToDTO(AirportMaster airport) {
        return new AirportMasterDTO(
                airport.getId(),
                airport.getAirportName(),
                airport.getAirportCode(),
                airport.getCity() != null ? airport.getCity().getId() : null,
                airport.getState() != null ? airport.getState().getId() : null,
                airport.getHub() != null ? airport.getHub().getId() : null
        );
    }
}