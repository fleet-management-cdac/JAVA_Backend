package com.example.service;

import com.example.dto.VehicleCatalogDTO;
import com.example.dto.VehicleDTO;
import com.example.entity.Vehicle;
import com.example.entity.VehicleRate;
import com.example.entity.VehicleStatus;
import com.example.entity.VehicleType;
import com.example.repository.VehicleRateRepository;
import com.example.repository.VehicleRepository;
import com.example.repository.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    private VehicleRateRepository vehicleRateRepository;

    @Autowired
    private VehicleRepository vehicleRepository;
    public List<VehicleDTO> getAvailableVehiclesForHandover(
            Long hubId, Long vehicleTypeId, LocalDate pickupDate, LocalDate returnDate) {

        List<Vehicle> vehicles;

        // Fetch based on filters
        if (hubId != null && vehicleTypeId != null) {
            vehicles = vehicleRepository.findByHubIdAndVehicleTypeIdAndStatus(
                    hubId, vehicleTypeId, VehicleStatus.available.name());
        } else if (hubId != null) {
            vehicles = vehicleRepository.findByHubIdAndStatus(hubId, VehicleStatus.available.name());
        } else if (vehicleTypeId != null) {
            vehicles = vehicleRepository.findByVehicleTypeIdAndStatus(vehicleTypeId, VehicleStatus.available.name());
        } else {
            vehicles = vehicleRepository.findByStatus(VehicleStatus.available.name());
        }

        // Filter out vehicles with maintenance in booking period
        if (pickupDate != null && returnDate != null) {
            vehicles = vehicles.stream()
                    .filter(v -> {
                        LocalDate serviceDate = v.getNextServiceDate();
                        if (serviceDate == null) return true; // No scheduled maintenance
                        // Exclude if service date is between pickup and return
                        return serviceDate.isBefore(pickupDate) || serviceDate.isAfter(returnDate);
                    })
                    .collect(Collectors.toList());
        }

        // Map to DTOs
        return vehicles.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    private VehicleDTO mapToDTO(Vehicle v) {
        VehicleDTO dto = new VehicleDTO();
        dto.setVehicleId(v.getId());
        dto.setCompany(v.getCompany());
        dto.setModel(v.getModel());
        dto.setRegistrationNo(v.getRegistrationNo());
        dto.setImgUrl(v.getImgUrl());
        dto.setStatus(v.getStatus());
        dto.setDescription(v.getDescription());
        dto.setNextServiceDate(v.getNextServiceDate());

        if (v.getVehicleType() != null) {
            dto.setVehicleTypeId(v.getVehicleType().getId());
            dto.setVehicleTypeName(v.getVehicleType().getTypeName());
        }
        if (v.getHub() != null) {
            dto.setHubId(v.getHub().getId());
            dto.setHubName(v.getHub().getHubName());
        }
        return dto;
    }
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