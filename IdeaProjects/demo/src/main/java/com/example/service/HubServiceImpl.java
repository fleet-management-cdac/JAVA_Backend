package com.example.service;

import com.example.dto.HubDto;
import com.example.repository.HubRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HubServiceImpl implements HubService {

    private final HubRepository hubRepository;

    public HubServiceImpl(HubRepository hubRepository) {
        this.hubRepository = hubRepository;
    }

    @Override
    public List<HubDto> getAllHubs() {
        return hubRepository.findAll()
                .stream()
                .map(h -> new HubDto(h.getId(), h.getHubName()))
                .toList();
    }

    @Override
    public List<HubDto> getHubsByCity(Long cityId) {
        return hubRepository.findByCity_Id(cityId)
                .stream()
                .map(h -> new HubDto(h.getId(), h.getHubName()))
                .toList();
    }
}