package com.example.service;

import com.example.dto.VehicleInventoryDTO;
import com.example.entity.Vehicle;
import com.example.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleInventoryService {

    @Autowired
    private VehicleRepository vehicleRepository;

    /**
     * Get all vehicles in a specific hub (regardless of status)
     */
    public List<VehicleInventoryDTO> getHubInventory(Long hubId) {
        List<Vehicle> vehicles = vehicleRepository.findByHub_Id(hubId);
        return vehicles.stream()
                .map(this::mapToInventoryDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all vehicles across all hubs
     */
    public List<VehicleInventoryDTO> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream()
                .map(this::mapToInventoryDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get vehicles by hub and status
     */
    public List<VehicleInventoryDTO> getVehiclesByHubAndStatus(Long hubId, String status) {
        List<Vehicle> vehicles = vehicleRepository.findByHubIdAndStatus(hubId, status);
        return vehicles.stream()
                .map(this::mapToInventoryDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get vehicles by hub and type
     */
    public List<VehicleInventoryDTO> getVehiclesByHubAndType(Long hubId, Long vehicleTypeId) {
        List<Vehicle> vehicles = vehicleRepository.findByHubIdAndVehicleTypeId(hubId, vehicleTypeId);
        return vehicles.stream()
                .map(this::mapToInventoryDTO)
                .collect(Collectors.toList());
    }

    /**
     * Map Vehicle entity to InventoryDTO
     */
    private VehicleInventoryDTO mapToInventoryDTO(Vehicle vehicle) {
        VehicleInventoryDTO dto = new VehicleInventoryDTO();

        dto.setVehicleId(vehicle.getId());
        dto.setCompany(vehicle.getCompany());
        dto.setModel(vehicle.getModel());
        dto.setRegistrationNo(vehicle.getRegistrationNo());
        dto.setImgUrl(vehicle.getImgUrl());
        dto.setStatus(vehicle.getStatus());
        dto.setDescription(vehicle.getDescription());
        dto.setLastServiceDate(vehicle.getLastServiceDate());
        dto.setNextServiceDate(vehicle.getNextServiceDate());

        // Vehicle Type Info
        if (vehicle.getVehicleType() != null) {
            dto.setVehicleTypeId(vehicle.getVehicleType().getId());
            dto.setVehicleTypeName(vehicle.getVehicleType().getTypeName());
        }

        // Hub Info
        if (vehicle.getHub() != null) {
            dto.setHubId(vehicle.getHub().getId());
            dto.setHubName(vehicle.getHub().getHubName());
            dto.setHubAddress(vehicle.getHub().getHubAddress());
        }

        return dto;
    }
}