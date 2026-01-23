package com.example.controllers;


import com.example.dto.ApiResponseDTO;
import com.example.dto.LanguageDTO;
import com.example.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/languages")
@CrossOrigin(origins = "*")
public class LanguageController {
    @Autowired
    private LanguageService languageService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<LanguageDTO>>> getAllLanguages() {
        List<LanguageDTO> languages = languageService.getAllLanguages();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Languages retrieved", languages));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<LanguageDTO>> getLanguageById(@PathVariable Long id) {
        LanguageDTO language = languageService.getLanguageById(id);
        if (language == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, "Language not found", null));
        }
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Language retrieved", language));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponseDTO<LanguageDTO>> getLanguageByCode(@PathVariable String code) {
        LanguageDTO language = languageService.getLanguageByCode(code);
        if (language == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, "Language not found", null));
        }
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Language retrieved", language));
    }

    @GetMapping("/default")
    public ResponseEntity<ApiResponseDTO<LanguageDTO>> getDefaultLanguage() {
        LanguageDTO language = languageService.getDefaultLanguage();
        if (language == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, "No default language set", null));
        }
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Default language retrieved", language));
    }
}