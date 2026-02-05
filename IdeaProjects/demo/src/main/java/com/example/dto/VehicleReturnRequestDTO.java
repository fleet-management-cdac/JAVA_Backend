package com.example.dto;

import java.time.LocalDate;

public class VehicleReturnRequestDTO {
    private Long bookingId;
    private Long processedBy; // Staff who processes return
    private LocalDate actualReturnDate; // Actual return date (can be different from booked date)
    private String fuelStatus; // Fuel status at return
    private Long staffHubId; // Hub where vehicle is actually returned (staff's hub)

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(Long processedBy) {
        this.processedBy = processedBy;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public String getFuelStatus() {
        return fuelStatus;
    }

    public void setFuelStatus(String fuelStatus) {
        this.fuelStatus = fuelStatus;
    }

    public Long getStaffHubId() {
        return staffHubId;
    }

    public void setStaffHubId(Long staffHubId) {
        this.staffHubId = staffHubId;
    }
}