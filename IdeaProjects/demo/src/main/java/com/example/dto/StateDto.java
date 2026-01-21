package com.example.dto;

public class StateDto {
    private Long id;
    private String stateName;

    public StateDto(Long id, String stateName) {
        this.id = id;
        this.stateName = stateName;
    }

    public Long getId() {
        return id;
    }

    public String getStateName() {
        return stateName;
    }
}