package com.example.service;

import com.example.dto.CityDto;
import java.util.List;

public interface CityService {

    List<CityDto> getAllCities();

    List<CityDto> getCitiesByState(Long stateId);
}