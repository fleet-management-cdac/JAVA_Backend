package com.example.service;

import com.example.dto.VehicleCatalogDTO;
import com.example.entity.VehicleRate;
import com.example.entity.VehicleStatus;
import com.example.entity.VehicleType;
import com.example.repository.VehicleRateRepository;
import com.example.repository.VehicleRepository;
import com.example.repository.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    private VehicleRateRepository vehicleRateRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<VehicleCatalogDTO> getVehicleCatalogData(Long hubId) {
        List<VehicleCatalogDTO> catalog = new ArrayList<>();
        List<VehicleType> allTypes = vehicleTypeRepository.findAll();

        for (VehicleType type : allTypes) {

            // 1. Calculate Availability
            long availableCount;
            if (hubId != null) {
                availableCount = vehicleRepository.countByVehicleTypeIdAndStatusAndHubId(
                        type.getId(), VehicleStatus.available.name(), hubId
                );
            } else {
                availableCount = vehicleRepository.countByVehicleTypeIdAndStatus(
                        type.getId(), VehicleStatus.available.name()
                );
            }

            // --- THE FIX ---
            // If the car is NOT available, skip it. Don't add it to the JSON.
            if (availableCount == 0) {
                continue;
            }
            // ---------------

            VehicleCatalogDTO dto = new VehicleCatalogDTO();

            // 2. Map Basic Info
            dto.setVehicleTypeId(type.getId());
            dto.setTypeName(type.getDescription());
            dto.setModelName(type.getTypeName());
            dto.setImgUrl(type.getImgUrl());

            // 3. Map Rates
            List<VehicleRate> rates = vehicleRateRepository.findByVehicleTypeId(type.getId());
            for (VehicleRate rate : rates) {
                if (rate.getPlans() != null) {
                    String plan = String.valueOf(rate.getPlans()).toLowerCase();
                    if (plan.contains("daily")) dto.setDailyRate(rate.getAmount());
                    else if (plan.contains("weekly")) dto.setWeeklyRate(rate.getAmount());
                    else if (plan.contains("monthly")) dto.setMonthlyRate(rate.getAmount());
                }
            }

            catalog.add(dto);
        }

        return catalog;
    }
}