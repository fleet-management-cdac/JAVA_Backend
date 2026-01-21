package com.example.service;

import com.example.dto.HubMasterDTO;
import com.example.entity.HubMaster;
import com.example.repository.HubRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class HubService {

    private final HubRepository hubRepository;

    public HubService(HubRepository hubRepository) {
        this.hubRepository = hubRepository;
    }

    public List<HubMasterDTO> getHubsByCity(Long cityId) {
        List<HubMaster> hubs = hubRepository.findByCity_Id(cityId);

        // Safety: Ensure we never return null to the Frontend
        if (hubs == null) return new ArrayList<>();

        return hubs.stream()
                .map(hub -> new HubMasterDTO(
                        hub.getId(),
                        hub.getHubName(),
                        hub.getHubAddress()
                ))
                .collect(Collectors.toList());
    }
}