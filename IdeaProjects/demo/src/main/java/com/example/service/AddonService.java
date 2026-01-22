package com.example.service;

import com.example.dto.AddonResponseDTO;
import com.example.dto.ApiResponseDTO;
import com.example.entity.Addon;
import com.example.repository.AddonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddonService {

    @Autowired
    private AddonRepository addonRepository;

    // GET ALL ADDONS
    public ApiResponseDTO<List<AddonResponseDTO>> getAllAddons() {
        List<Addon> addons = addonRepository.findAll();
        List<AddonResponseDTO> responses = new ArrayList<>();

        for (Addon addon : addons) {
            responses.add(mapToDTO(addon));
        }

        return ApiResponseDTO.success("Addons fetched", responses);
    }

    // GET ADDON BY ID
    public ApiResponseDTO<AddonResponseDTO> getAddonById(Long addonId) {
        Optional<Addon> addonOpt = addonRepository.findById(addonId);

        if (addonOpt.isEmpty()) {
            return ApiResponseDTO.error("Addon not found");
        }

        return ApiResponseDTO.success("Addon fetched", mapToDTO(addonOpt.get()));
    }

    private AddonResponseDTO mapToDTO(Addon addon) {
        AddonResponseDTO dto = new AddonResponseDTO();
        dto.setAddonId(addon.getId());
        dto.setName(addon.getName());
        dto.setDescription(addon.getDescription());
        dto.setPricePerDay(addon.getPricePerDay());
        return dto;
    }
}