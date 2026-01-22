package com.example.service;

import com.example.dto.HubMasterDTO;
import com.example.entity.CityMaster;
import com.example.entity.HubMaster;
import com.example.entity.StateMaster;
import com.example.repository.HubMasterRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.example.entity.HubMaster;
import com.example.repository.HubMasterRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class HubService {

    private final HubMasterRepository hubRepository;

    public HubService(HubMasterRepository hubRepository) {
        this.hubRepository = hubRepository;
    }

    // --- EXISTING FETCH METHOD ---
    public List<HubMasterDTO> getHubsByCity(Long cityId) {
        List<HubMaster> hubs = hubRepository.findByCity_Id(cityId);
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

    // --- UPDATED EXCEL UPLOAD METHOD (SAFE FOR DUPLICATES) ---
    @Transactional
    public void saveHubsFromExcel(MultipartFile file) {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            System.out.println("DEBUG: Starting Excel Processing...");

            // Skip Header Row
            if (rows.hasNext()) {
                rows.next();
            }

            List<HubMaster> hubsToSave = new ArrayList<>();

            while (rows.hasNext()) {
                Row row = rows.next();

                // 1. Read Cells safely
                String name = getCellValue(row.getCell(0));
                String address = getCellValue(row.getCell(1));
                String cityIdStr = getCellValue(row.getCell(2));
                String stateIdStr = getCellValue(row.getCell(3));

                // Basic Validation
                if (name != null && !name.isEmpty() && cityIdStr != null && !cityIdStr.isEmpty()) {
                    try {
                        Long cityId = (long) Double.parseDouble(cityIdStr);

                        // --- 2. CHECK DB: Get LIST of existing hubs ---
                        // This prevents the "NonUniqueResultException" crash
                        List<HubMaster> existingHubs = hubRepository.findByHubNameAndCity_Id(name, cityId);
                        HubMaster hub;

                        if (!existingHubs.isEmpty()) {
                            // If duplicates exist, just pick the FIRST one to update
                            hub = existingHubs.get(0);
                            System.out.println("DEBUG: Found existing Hub (ID: " + hub.getId() + "). Updating...");
                        } else {
                            // If list is empty, create NEW
                            hub = new HubMaster();
                            System.out.println("DEBUG: Creating NEW Hub: " + name);
                        }

                        // --- 3. Update Fields ---
                        hub.setHubName(name);
                        hub.setHubAddress(address);

                        // Set City
                        CityMaster city = new CityMaster();
                        city.setId(cityId);
                        hub.setCity(city);

                        // Set State
                        if (stateIdStr != null && !stateIdStr.isEmpty()) {
                            Long stateId = (long) Double.parseDouble(stateIdStr);
                            StateMaster state = new StateMaster();
                            state.setId(stateId);
                            hub.setState(state);
                        }

                        hubsToSave.add(hub);

                    } catch (NumberFormatException e) {
                        System.out.println("ERROR: Invalid Number format in row: " + name);
                    }
                }
            }

            // --- 4. Save All ---
            if (!hubsToSave.isEmpty()) {
                hubRepository.saveAll(hubsToSave);
                System.out.println("DEBUG: Database Sync Complete. Processed " + hubsToSave.size() + " items.");
            } else {
                System.out.println("DEBUG: No valid data found to save.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }

    // Helper method
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return cell.getStringCellValue().trim();
    }
}