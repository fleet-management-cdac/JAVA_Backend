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

import java.math.BigDecimal;
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

    /**
     * Combines Vehicle Types, Rates, and Availability into a single list
     * for the "Vehicle Selection" frontend page.
     */
    public List<VehicleCatalogDTO> getVehicleCatalogData() {
        List<VehicleCatalogDTO> catalog = new ArrayList<>();

        // 1. Get all base Vehicle Types (e.g., Small Cars, SUVs)
        List<VehicleType> allTypes = vehicleTypeRepository.findAll();

        for (VehicleType type : allTypes) {
            VehicleCatalogDTO dto = new VehicleCatalogDTO();

            // 2. Map Basic Info from VehicleType
            dto.setVehicleTypeId(type.getId());
            // Based on your DB, 'description' is likely the Category (e.g., Small Cars)
            dto.setCategoryName(type.getDescription()); 
            // 'typeName' is likely the model example (e.g., Chevrolet Aveo)
            dto.setModelName(type.getTypeName());       
            dto.setImgUrl(type.getImgUrl());

            // 3. Fetch and Pivot Rates (Rows to Columns)
            List<VehicleRate> rates = vehicleRateRepository.findByVehicleTypeId(type.getId());
            
            for (VehicleRate rate : rates) {
                // Convert Enum/String to lowercase safely to check plan type
                String plan = String.valueOf(rate.getPlans()).toLowerCase(); 
                
                if (plan.contains("daily")) {
                    dto.setDailyRate(rate.getAmount());
                } else if (plan.contains("weekly")) {
                    dto.setWeeklyRate(rate.getAmount());
                } else if (plan.contains("monthly")) {
                    dto.setMonthlyRate(rate.getAmount());
                }
            }

            // 4. Determine Availability Status (Business Logic)
            // Count how many cars of this specific type are physically 'available'
            long availableCount = vehicleRepository.countByVehicleTypeIdAndStatus(
                    type.getId(),
                    VehicleStatus.available.name()
            );

            if (availableCount > 0) {
                dto.setStatusButtonLabel("Select");
                dto.setSelectable(true);
            } else {
                // If 0 cars are available, we mark as "Sold" (Rented out)
                dto.setStatusButtonLabel("Sold");
                dto.setSelectable(false);
            }

            catalog.add(dto);
        }

        return catalog;
    }
}