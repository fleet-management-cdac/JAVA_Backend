package com.example.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationDTO {
    private Long translationId;
    private Long languageId;
    private String tKey;
    private String tValue;
}