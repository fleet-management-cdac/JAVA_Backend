package com.example.controllers;

import com.example.dto.ApiResponseDTO;
import com.example.dto.LanguageDTO;
import com.example.service.LanguageService;
import com.example.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/i18n")
@CrossOrigin(origins = "*")
public class I18nController {

    @Autowired
    private LanguageService languageService;

    @Autowired
    private TranslationService translationService;

    /**
     * Get all available languages
     * GET /api/i18n/languages
     */
    @GetMapping("/languages")
    public ApiResponseDTO<List<LanguageDTO>> getAllLanguages() {
        List<LanguageDTO> languages = languageService.getAllLanguages();
        return ApiResponseDTO.success("Languages retrieved", languages);
    }

    /**
     * Get all translations for a language code

     */
    @GetMapping("/translations/{code}")
    public ApiResponseDTO<Map<String, String>> getTranslations(@PathVariable String code) {
        Map<String, String> translations = translationService.getTranslationsMapByLanguageCode(code);
        if (translations == null || translations.isEmpty()) {
            return ApiResponseDTO.error("Language not found or no translations available");
        }
        return ApiResponseDTO.success("Translations retrieved", translations);
    }
}