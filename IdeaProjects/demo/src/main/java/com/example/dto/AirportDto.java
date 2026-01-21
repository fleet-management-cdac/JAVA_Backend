package com.example.dto;

public class AirportDto {

    private Long id;
    private String airportName;
    private String airportCode;

    public AirportDto(Long id, String airportName, String airportCode) {
        this.id = id;
        this.airportName = airportName;
        this.airportCode = airportCode;
    }

    public Long getId() {
        return id;
    }

    public String getAirportName() {
        return airportName;
    }

    public String getAirportCode() {
        return airportCode;
    }
}