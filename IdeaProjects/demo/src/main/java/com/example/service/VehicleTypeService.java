package com.example.service;

import com.example.dto.VehicleTypeWithRatesDTO;
import com.example.entity.VehicleRate;
import com.example.entity.VehicleType;
import com.example.repository.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleTypeService {

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    /**
     * Get all vehicle types with their daily, weekly, monthly rates
     */
    public List<VehicleTypeWithRatesDTO> getAllVehicleTypesWithRates() {
        List<VehicleType> vehicleTypes = vehicleTypeRepository.findAllWithRates();
        List<VehicleTypeWithRatesDTO> result = new ArrayList<>();

        for (VehicleType vt : vehicleTypes) {
            VehicleTypeWithRatesDTO dto = new VehicleTypeWithRatesDTO();
            dto.setVehicleTypeId(vt.getId());
            dto.setTypeName(vt.getTypeName());
            dto.setDescription(vt.getDescription());
            dto.setImgUrl(vt.getImgUrl());

            // Map rates by plan type
            if (vt.getVehicleRates() != null) {
                for (VehicleRate rate : vt.getVehicleRates()) {
                    String plan = rate.getPlans().toLowerCase().trim();
                    switch (plan) {
                        case "daily":
                            dto.setDailyRate(rate.getAmount());
                            break;
                        case "weekly":
                            dto.setWeeklyRate(rate.getAmount());
                            break;
                        case "monthly":
                            dto.setMonthlyRate(rate.getAmount());
                            break;
                    }
                }
            }

            // Set default 0 for missing rates
            if (dto.getDailyRate() == null)
                dto.setDailyRate(BigDecimal.ZERO);
            if (dto.getWeeklyRate() == null)
                dto.setWeeklyRate(BigDecimal.ZERO);
            if (dto.getMonthlyRate() == null)
                dto.setMonthlyRate(BigDecimal.ZERO);

            result.add(dto);
        }

        return result;
    }
}
