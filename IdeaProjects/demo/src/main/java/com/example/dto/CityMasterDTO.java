package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor // Added for JSON deserialization support
public class CityMasterDTO {
    private Long id;
    private String cityName;
}