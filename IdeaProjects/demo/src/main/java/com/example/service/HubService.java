package com.example.service;

import com.example.dto.HubMasterDTO;
import com.example.entity.CityMaster;
import com.example.entity.HubMaster;
import com.example.entity.StateMaster;
import com.example.repository.HubMasterRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HubService {

    private final HubMasterRepository hubRepository;
    private final RestTemplate restTemplate; // 1. Declare RestTemplate

    // 2. Inject RestTemplate via Constructor
    public HubService(HubMasterRepository hubRepository, RestTemplate restTemplate) {
        this.hubRepository = hubRepository;
        this.restTemplate = restTemplate;
    }

    // 3. Define .NET API URL (Add "dotnet.api.url" to your application.properties)
    // Example: dotnet.api.url=http://localhost:5000/api/hubs/sync
    @Value("${dotnet.api.url:http://localhost:5000/api/hubs/sync}")
    private String dotNetApiUrl;

    // --- EXISTING FETCH METHOD ---
    public List<HubMasterDTO> getHubsByCity(Long cityId) {
        List<HubMaster> hubs = hubRepository.findByCity_Id(cityId);

        if (hubs == null) return new ArrayList<>();

        return hubs.stream()
                .map(hub -> new HubMasterDTO(
                        hub.getId(),
                        hub.getHubName(),
                        hub.getHubAddress()
                ))
                .collect(Collectors.toList());
    }

    // --- UPDATED EXCEL UPLOAD METHOD ---
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

                String name = getCellValue(row.getCell(0));
                String address = getCellValue(row.getCell(1));
                String cityIdStr = getCellValue(row.getCell(2));
                String stateIdStr = getCellValue(row.getCell(3));

                if (name != null && !name.isEmpty() && cityIdStr != null && !cityIdStr.isEmpty()) {
                    try {
                        Long cityId = (long) Double.parseDouble(cityIdStr);
                        // Check for duplicates
                        List<HubMaster> existingHubs = hubRepository.findByHubNameAndCity_Id(name, cityId);
                        HubMaster hub;

                        if (!existingHubs.isEmpty()) {
                            hub = existingHubs.get(0);
                            System.out.println("DEBUG: Updating Hub ID: " + hub.getId());
                        } else {
                            hub = new HubMaster();
                            System.out.println("DEBUG: Creating NEW Hub: " + name);
                        }

                        hub.setHubName(name);
                        hub.setHubAddress(address);

                        CityMaster city = new CityMaster();
                        city.setId(cityId);
                        hub.setCity(city);

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

            // --- 4. Save to MySQL & Call .NET API ---
            if (!hubsToSave.isEmpty()) {
                // A. Save to Local MySQL
                List<HubMaster> savedHubs = hubRepository.saveAll(hubsToSave);
                System.out.println("DEBUG: MySQL Sync Complete. Saved " + savedHubs.size() + " items.");

                // B. Call .NET API
                syncWithDotNetApi(savedHubs);

            } else {
                System.out.println("DEBUG: No valid data found to save.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }

    // --- 5. New Method to Call .NET API ---
    private void syncWithDotNetApi(List<HubMaster> hubs) {
        try {
            System.out.println("DEBUG: Initiating call to .NET API at " + dotNetApiUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Wrap the list in HttpEntity
            HttpEntity<List<HubMaster>> requestEntity = new HttpEntity<>(hubs, headers);

            // Make POST call
            String response = restTemplate.postForObject(dotNetApiUrl, requestEntity, String.class);

            System.out.println("DEBUG: .NET API Response: " + response);

        } catch (Exception e) {
            // Log error but do not fail the transaction, so MySQL data remains saved
            System.err.println("ERROR: Failed to sync with .NET API: " + e.getMessage());
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return cell.getStringCellValue().trim();
    }
}
