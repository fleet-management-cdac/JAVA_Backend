package com.example.service;

import com.example.dto.HubMasterDTO;
import com.example.entity.HubMaster;
import com.example.repository.HubMasterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HubService {

    private static final Logger logger = LoggerFactory.getLogger(HubService.class);
    private final HubMasterRepository hubRepository;

    public HubService(HubMasterRepository hubRepository) {
        this.hubRepository = hubRepository;
    }

    public List<HubMasterDTO> getHubsByCity(Long cityId) {
        logger.info("Fetching hubs for cityId: {}", cityId);

        List<HubMaster> hubs = hubRepository.findByCity_Id(cityId);

        logger.debug("Found {} hubs for cityId: {}", hubs.size(), cityId);

        return hubs.stream()
                .map(hub -> new HubMasterDTO(
                        hub.getId(),
                        hub.getHubName(),
                        hub.getHubAddress()
                ))
                .collect(Collectors.toList());
    }
}