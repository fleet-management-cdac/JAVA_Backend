package com.example.dto;

import java.math.BigDecimal;

public class VehicleCatalogDTO {

    private Long vehicleTypeId;
    private String typeName;      // e.g., "Small Cars"
    private String modelName;         // e.g., "Chevrolet Aveo or similar"
    private String imgUrl;

    // Flattened Rates
    private BigDecimal dailyRate;
    private BigDecimal weeklyRate;
    private BigDecimal monthlyRate;

    // Computed Status for UI (Select / Sold / N.A)


    // Constructors
    public VehicleCatalogDTO() {}

    // Getters and Setters
    public Long getVehicleTypeId() { return vehicleTypeId; }
    public void setVehicleTypeId(Long vehicleTypeId) { this.vehicleTypeId = vehicleTypeId; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

    public BigDecimal getDailyRate() { return dailyRate; }
    public void setDailyRate(BigDecimal dailyRate) { this.dailyRate = dailyRate; }

    public BigDecimal getWeeklyRate() { return weeklyRate; }
    public void setWeeklyRate(BigDecimal weeklyRate) { this.weeklyRate = weeklyRate; }

    public BigDecimal getMonthlyRate() { return monthlyRate; }
    public void setMonthlyRate(BigDecimal monthlyRate) { this.monthlyRate = monthlyRate; }


}