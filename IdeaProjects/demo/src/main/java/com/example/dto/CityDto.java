package com.example.dto;

public class CityDto {
    private Long id;
    private String cityName;

    public CityDto(Long id, String cityName) {
        this.id = id;
        this.cityName = cityName;
    }

    public Long getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }
}