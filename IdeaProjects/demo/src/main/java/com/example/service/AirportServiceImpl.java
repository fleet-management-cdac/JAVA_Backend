package com.example.service;

import com.example.dto.AirportDto;
import com.example.repository.AirportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;

    public AirportServiceImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
    public List<AirportDto> getAllAirports() {
        return airportRepository.findAll()
                .stream()
                .map(a -> new AirportDto(
                        a.getId(),
                        a.getAirportName(),
                        a.getAirportCode()
                ))
                .toList();
    }
}