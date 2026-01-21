package com.example.dto;

public class HubDto {
    private Long id;
    private String hubName;

    public HubDto(Long id, String hubName) {
        this.id = id;
        this.hubName = hubName;
    }

    public Long getId() {
        return id;
    }

    public String getHubName() {
        return hubName;
    }
}