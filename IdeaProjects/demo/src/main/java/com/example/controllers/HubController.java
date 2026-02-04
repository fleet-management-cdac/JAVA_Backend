package com.example.controllers;

import com.example.dto.HubMasterDTO;
import com.example.service.HubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class HubController {

    private static final Logger logger = LoggerFactory.getLogger(HubController.class);

    private final HubService hubService;
    private final RestTemplate restTemplate;

    @Value("${microservice.upload.url:http://localhost:5123/api/hubs/upload}")
    private String microserviceUploadUrl;

    public HubController(HubService hubService) {
        this.hubService = hubService;
        this.restTemplate = new RestTemplate();
    }

    // --- GET hubs by city ---
    @GetMapping("/hubs/city/{cityId}")
    public ResponseEntity<List<HubMasterDTO>> getHubsByCity(@PathVariable Long cityId) {
        List<HubMasterDTO> hubs = hubService.getHubsByCity(cityId);
        return ResponseEntity.ok(hubs);
    }

    // --- UPLOAD METHOD - Forwards file to .NET Microservice ---
    @PostMapping("/upload")
    public ResponseEntity<?> uploadHubs(@RequestParam("file") MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            logger.info("Received file upload: {} ({} bytes)", filename, file.getSize());

            // Prepare multipart request to send to microservice
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Create the file resource
            ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            // Build multipart body
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileResource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Forward to microservice
            logger.info("Forwarding file to microservice: {}", microserviceUploadUrl);
            ResponseEntity<Map> response = restTemplate.exchange(
                    microserviceUploadUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Microservice processed file successfully");
                return ResponseEntity.ok(response.getBody());
            } else {
                logger.warn("Microservice returned error: {}", response.getStatusCode());
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }

        } catch (org.springframework.web.client.ResourceAccessException e) {
            logger.error("Failed to connect to microservice: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of(
                            "success", false,
                            "message", "Microservice unavailable. Please try again later."
                    ));
        } catch (Exception e) {
            logger.error("Error during file upload", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Error uploading file: " + e.getMessage()
                    ));
        }
    }
}