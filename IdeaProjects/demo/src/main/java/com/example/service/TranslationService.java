package com.example.service;

import com.example.dto.LanguageDTO;
import com.example.dto.TranslationDTO;
import com.example.dto.TranslationMapDTO;
import com.example.entity.Language;
import com.example.entity.Translation;
import com.example.repository.LanguageRepository;
import com.example.repository.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TranslationService {

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageService languageService;

    // Primary translation lookup method
    public String translate(String key, String languageCode) {
        return translationRepository.findByTKeyAndLanguageCode(key, languageCode)
                .map(Translation::getTValue)
                .orElseGet(() -> {
                    // Fallback to default language
                    return languageRepository.findByIsDefaultTrue()
                            .flatMap(defaultLang ->
                                    translationRepository.findByTKeyAndLanguageCode(key, defaultLang.getCode()))
                            .map(Translation::getTValue)
                            .orElse(key);
                });
    }

    public List<TranslationDTO> getTranslationsByLanguageId(Long languageId) {
        return translationRepository.findByLanguageId(languageId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Map<String, String> getTranslationsMapByLanguageId(Long languageId) {
        return translationRepository.findByLanguageId(languageId)
                .stream()
                .collect(Collectors.toMap(
                        Translation::getTKey,
                        Translation::getTValue,
                        (existing, replacement) -> existing
                ));
    }

    public Map<String, String> getTranslationsMapByLanguageCode(String languageCode) {
        return translationRepository.findAllByLanguageCode(languageCode)
                .stream()
                .collect(Collectors.toMap(
                        Translation::getTKey,
                        Translation::getTValue,
                        (existing, replacement) -> existing
                ));
    }

    public TranslationMapDTO getTranslationsWithMetadata(Long languageId) {
        LanguageDTO language = languageService.getLanguageById(languageId);
        if (language == null) {
            return null;
        }
        Map<String, String> translations = getTranslationsMapByLanguageId(languageId);
        return new TranslationMapDTO(
                language.getLanguageId(),
                language.getCode(),
                language.getName(),
                translations
        );
    }

    public TranslationMapDTO getTranslationsByLanguageCode(String code) {
        LanguageDTO language = languageService.getLanguageByCode(code);
        if (language == null) {
            return null;
        }
        Map<String, String> translations = getTranslationsMapByLanguageId(language.getLanguageId());
        return new TranslationMapDTO(
                language.getLanguageId(),
                language.getCode(),
                language.getName(),
                translations
        );
    }

    public TranslationDTO getTranslationById(Long id) {
        return translationRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Transactional
    public TranslationDTO createTranslation(TranslationDTO translationDTO) {
        Language language = languageRepository.findById(translationDTO.getLanguageId())
                .orElseThrow(() -> new IllegalArgumentException("Language not found"));

        if (translationRepository.existsByTKeyAndLanguageId(translationDTO.getTKey(), language.getId())) {
            throw new IllegalArgumentException("Translation already exists for this key and language");
        }

        Translation translation = new Translation();
        translation.setLanguage(language);
        translation.setTKey(translationDTO.getTKey());
        translation.setTValue(translationDTO.getTValue());

        Translation savedTranslation = translationRepository.save(translation);
        return convertToDTO(savedTranslation);
    }

    @Transactional
    public TranslationDTO updateTranslation(Long id, TranslationDTO translationDTO) {
        Translation translation = translationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Translation not found"));

        translation.setTKey(translationDTO.getTKey());
        translation.setTValue(translationDTO.getTValue());

        if (translationDTO.getLanguageId() != null &&
                !translationDTO.getLanguageId().equals(translation.getLanguage().getId())) {
            Language language = languageRepository.findById(translationDTO.getLanguageId())
                    .orElseThrow(() -> new IllegalArgumentException("Language not found"));
            translation.setLanguage(language);
        }

        Translation updatedTranslation = translationRepository.save(translation);
        return convertToDTO(updatedTranslation);
    }

    @Transactional
    public TranslationDTO upsertTranslation(String key, String languageCode, String value) {
        Language language = languageRepository.findByCode(languageCode)
                .orElseThrow(() -> new IllegalArgumentException("Language not found"));

        Translation translation = translationRepository.findByTKeyAndLanguageId(key, language.getId())
                .orElseGet(() -> {
                    Translation newTranslation = new Translation();
                    newTranslation.setLanguage(language);
                    newTranslation.setTKey(key);
                    return newTranslation;
                });

        translation.setTValue(value);
        Translation savedTranslation = translationRepository.save(translation);
        return convertToDTO(savedTranslation);
    }

    @Transactional
    public List<TranslationDTO> bulkUpsertTranslations(String languageCode, Map<String, String> translationsMap) {
        Language language = languageRepository.findByCode(languageCode)
                .orElseThrow(() -> new IllegalArgumentException("Language not found"));

        return translationsMap.entrySet().stream()
                .map(entry -> {
                    Translation translation = translationRepository.findByTKeyAndLanguageId(entry.getKey(), language.getId())
                            .orElseGet(() -> {
                                Translation newTranslation = new Translation();
                                newTranslation.setLanguage(language);
                                newTranslation.setTKey(entry.getKey());
                                return newTranslation;
                            });
                    translation.setTValue(entry.getValue());
                    return translationRepository.save(translation);
                })
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTranslation(Long id) {
        if (!translationRepository.existsById(id)) {
            throw new IllegalArgumentException("Translation not found");
        }
        translationRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllTranslationsForKey(String key) {
        translationRepository.deleteAllByTKey(key);
    }

    private TranslationDTO convertToDTO(Translation translation) {
        return new TranslationDTO(
                translation.getId(),
                translation.getLanguage().getId(),
                translation.getTKey(),
                translation.getTValue()
        );
    }
}