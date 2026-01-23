package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationMapDTO {
    private Long languageId;
    private String languageCode;
    private String languageName;
    private Map<String, String> translations;
}