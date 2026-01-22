package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HubMasterDTO {
    private Long id;
    private String hubName;
    private String hubAddress;
}