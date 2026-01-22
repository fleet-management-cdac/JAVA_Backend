package com.example.controllers;

import com.example.dto.ApiResponseDTO;
import com.example.dto.TranslationDTO;
import com.example.dto.TranslationMapDTO;
import com.example.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/translations")
@CrossOrigin(origins = "*")
public class TranslationController {
    @Autowired
    private TranslationService translationService;

    // Get single translation value
    @GetMapping("/translate")
    public ResponseEntity<ApiResponseDTO<String>> translate(
            @RequestParam String key,
            @RequestParam(defaultValue = "en") String lang) {
        String value = translationService.translate(key, lang);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Translation retrieved", value));
    }

    // Get all translations as key-value map (for frontend)
    @GetMapping("/all/{languageCode}")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> getAllTranslationsByCode(
            @PathVariable String languageCode) {
        Map<String, String> translations = translationService.getTranslationsMapByLanguageCode(languageCode);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Translations retrieved", translations));
    }

    // Get all translations with metadata by language code
    @GetMapping("/code/{languageCode}")
    public ResponseEntity<ApiResponseDTO<TranslationMapDTO>> getTranslationsByCode(
            @PathVariable String languageCode) {
        TranslationMapDTO result = translationService.getTranslationsByLanguageCode(languageCode);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, "Language not found", null));
        }
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Translations retrieved", result));
    }

    // Get all translations by language ID
    @GetMapping("/language/{languageId}")
    public ResponseEntity<ApiResponseDTO<List<TranslationDTO>>> getTranslationsByLanguageId(
            @PathVariable Long languageId) {
        List<TranslationDTO> translations = translationService.getTranslationsByLanguageId(languageId);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Translations retrieved", translations));
    }

    // Get translation by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TranslationDTO>> getTranslationById(@PathVariable Long id) {
        TranslationDTO translation = translationService.getTranslationById(id);
        if (translation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, "Translation not found", null));
        }
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Translation retrieved", translation));
    }
}