package com.example.dto;

public class HandoverRequestDTO {
    private Long bookingId;
    private Long processedBy;
    private String fuelStatus;

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Long getProcessedBy() { return processedBy; }
    public void setProcessedBy(Long processedBy) { this.processedBy = processedBy; }

    public String getFuelStatus() { return fuelStatus; }
    public void setFuelStatus(String fuelStatus) { this.fuelStatus = fuelStatus; }
}