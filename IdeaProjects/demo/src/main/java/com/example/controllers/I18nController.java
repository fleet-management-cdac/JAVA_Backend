package com.example.controllers;

import com.example.dto.LanguageDTO;
import com.example.dto.TranslationDTO;
import com.example.dto.TranslationMapDTO;
import com.example.service.LanguageService;
import com.example.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    // ============================================
    // LANGUAGE ENDPOINTS
    // ============================================

//    /**
//     * Get all languages
//     * GET /api/i18n/languages
//     */
//    @GetMapping("/languages")
//    public ResponseEntity<List<LanguageDTO>> getAllLanguages() {
//        List<LanguageDTO> languages = languageService.getAllLanguages();
//        return ResponseEntity.ok(languages);
//    }

    /**
     * Get all language codes
     * GET /api/i18n/languages/codes
     */
    @GetMapping("/languages/codes")
    public ResponseEntity<List<String>> getAllLanguageCodes() {
        List<String> codes = languageService.getAllLanguageCodes();
        return ResponseEntity.ok(codes);
    }

//    /**
//     * Get language by ID
//     * GET /api/i18n/languages/{id}
//     */
//    @GetMapping("/languages/{id}")
//    public ResponseEntity<LanguageDTO> getLanguageById(@PathVariable Long id) {
//        LanguageDTO language = languageService.getLanguageById(id);
//        if (language == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(language);
//    }

//    /**
//     * Get language by code
//     * GET /api/i18n/languages/code/{code}
//     */
//    @GetMapping("/languages/code/{code}")
//    public ResponseEntity<LanguageDTO> getLanguageByCode(@PathVariable String code) {
//        LanguageDTO language = languageService.getLanguageByCode(code);
//        if (language == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(language);
//    }

//    /**
//     * Get default language
//     * GET /api/i18n/languages/default
//     */
//    @GetMapping("/languages/default")
//    public ResponseEntity<LanguageDTO> getDefaultLanguage() {
//        LanguageDTO language = languageService.getDefaultLanguage();
//        if (language == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(language);
//    }

    // ============================================
    // TRANSLATION ENDPOINTS
    // ============================================

//    /**
//     * Get translations by language ID (returns list)
//     * GET /api/i18n/translations/{languageId}
//     */
//    @GetMapping("/translations/{languageId}")
//    public ResponseEntity<List<TranslationDTO>> getTranslationsByLanguageId(
//            @PathVariable Long languageId) {
//        List<TranslationDTO> translations = translationService.getTranslationsByLanguageId(languageId);
//        return ResponseEntity.ok(translations);
//    }

    /**
     * Get translations by language ID (returns map)
     * GET /api/i18n/translations/{languageId}/map
     */
    @GetMapping("/translations/{languageId}/map")
    public ResponseEntity<Map<String, String>> getTranslationsMapByLanguageId(
            @PathVariable Long languageId) {
        Map<String, String> translations = translationService.getTranslationsMapByLanguageId(languageId);
        return ResponseEntity.ok(translations);
    }

    /**
     * Get translations with metadata by language ID
     * GET /api/i18n/translations/{languageId}/full
     */
    @GetMapping("/translations/{languageId}/full")
    public ResponseEntity<TranslationMapDTO> getTranslationsWithMetadata(
            @PathVariable Long languageId) {
        TranslationMapDTO translations = translationService.getTranslationsWithMetadata(languageId);
        if (translations == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(translations);
    }

//    /**
//     * Get translations by language code
//     * GET /api/i18n/translations/code/{code}
//     */
//    @GetMapping("/translations/code/{code}")
//    public ResponseEntity<TranslationMapDTO> getTranslationsByLanguageCode(
//            @PathVariable String code) {
//        TranslationMapDTO translations = translationService.getTranslationsByLanguageCode(code);
//        if (translations == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(translations);
//    }
}