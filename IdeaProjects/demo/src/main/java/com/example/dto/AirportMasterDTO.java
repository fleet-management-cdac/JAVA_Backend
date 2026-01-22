package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportMasterDTO {
    private Long id;
    private String airportName;
    private String airportCode; // Included BOM, PNQ, etc.
    private Long cityId;
    private Long stateId;
    private Long hubId;
}