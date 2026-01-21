package com.example.service;

import com.example.dto.CityDto;
import com.example.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<CityDto> getAllCities() {
        return cityRepository.findAll()
                .stream()
                .map(c -> new CityDto(c.getId(), c.getCityName()))
                .toList();
    }

    @Override
    public List<CityDto> getCitiesByState(Long stateId) {
        return cityRepository.findByState_Id(stateId)
                .stream()
                .map(c -> new CityDto(c.getId(), c.getCityName()))
                .toList();
    }
}