package com.example.service;


import com.example.dto.LanguageDTO;
import com.example.entity.Language;
import com.example.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LanguageService {

    @Autowired
    private LanguageRepository languageRepository;

    public List<LanguageDTO> getAllLanguages() {
        return languageRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<String> getAllLanguageCodes() {
        return languageRepository.findAll()
                .stream()
                .map(Language::getCode)
                .collect(Collectors.toList());
    }

    public LanguageDTO getLanguageById(Long id) {
        return languageRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public LanguageDTO getLanguageByCode(String code) {
        return languageRepository.findByCode(code)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public LanguageDTO getDefaultLanguage() {
        return languageRepository.findByIsDefaultTrue()
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Transactional
    public LanguageDTO createLanguage(LanguageDTO languageDTO) {
        if (languageRepository.existsByCode(languageDTO.getCode())) {
            throw new IllegalArgumentException("Language with code '" + languageDTO.getCode() + "' already exists");
        }

        Language language = new Language();
        language.setCode(languageDTO.getCode());
        language.setName(languageDTO.getName());
        language.setIsDefault(languageDTO.getIsDefault() != null ? languageDTO.getIsDefault() : false);

        if (Boolean.TRUE.equals(language.getIsDefault())) {
            unsetCurrentDefault();
        }

        Language savedLanguage = languageRepository.save(language);
        return convertToDTO(savedLanguage);
    }

    @Transactional
    public LanguageDTO updateLanguage(Long id, LanguageDTO languageDTO) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Language not found with ID: " + id));

        if (!language.getCode().equals(languageDTO.getCode()) &&
                languageRepository.existsByCode(languageDTO.getCode())) {
            throw new IllegalArgumentException("Language with code '" + languageDTO.getCode() + "' already exists");
        }

        language.setCode(languageDTO.getCode());
        language.setName(languageDTO.getName());

        if (Boolean.TRUE.equals(languageDTO.getIsDefault()) && !Boolean.TRUE.equals(language.getIsDefault())) {
            unsetCurrentDefault();
        }
        language.setIsDefault(languageDTO.getIsDefault());

        Language updatedLanguage = languageRepository.save(language);
        return convertToDTO(updatedLanguage);
    }

    @Transactional
    public void deleteLanguage(Long id) {
        if (!languageRepository.existsById(id)) {
            throw new IllegalArgumentException("Language not found with ID: " + id);
        }
        languageRepository.deleteById(id);
    }

    @Transactional
    public LanguageDTO setDefaultLanguage(Long id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Language not found with ID: " + id));

        unsetCurrentDefault();
        language.setIsDefault(true);

        Language updatedLanguage = languageRepository.save(language);
        return convertToDTO(updatedLanguage);
    }

    private void unsetCurrentDefault() {
        languageRepository.findByIsDefaultTrue().ifPresent(defaultLang -> {
            defaultLang.setIsDefault(false);
            languageRepository.save(defaultLang);
        });
    }

    private LanguageDTO convertToDTO(Language language) {
        return new LanguageDTO(
                language.getId(),
                language.getCode(),
                language.getName(),
                language.getIsDefault()
        );
    }
}