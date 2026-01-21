package com.example.service;

import com.example.dto.HubDto;
import java.util.List;

public interface HubService {

    List<HubDto> getAllHubs();

    List<HubDto> getHubsByCity(Long cityId);
}