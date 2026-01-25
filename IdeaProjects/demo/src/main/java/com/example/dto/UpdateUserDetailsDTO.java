package com.example.dto;

import java.time.LocalDate;

public class UpdateUserDetailsDTO {
    private String firstName;
    private String lastName;
    private String address;
    private Long cityId;
    private String zipcode;
    private String phoneHome;
    private String phoneCell;
    private LocalDate dateOfBirth;
    private String drivingLicenseNo;
    private LocalDate licenseValidTill;
    private String passportNo;
    private LocalDate passportValidTill;
    private Long dipNumber;
    private LocalDate dipValidTill;

    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }

    public String getZipcode() { return zipcode; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }

    public String getPhoneHome() { return phoneHome; }
    public void setPhoneHome(String phoneHome) { this.phoneHome = phoneHome; }

    public String getPhoneCell() { return phoneCell; }
    public void setPhoneCell(String phoneCell) { this.phoneCell = phoneCell; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getDrivingLicenseNo() { return drivingLicenseNo; }
    public void setDrivingLicenseNo(String drivingLicenseNo) { this.drivingLicenseNo = drivingLicenseNo; }

    public LocalDate getLicenseValidTill() { return licenseValidTill; }
    public void setLicenseValidTill(LocalDate licenseValidTill) { this.licenseValidTill = licenseValidTill; }

    public String getPassportNo() { return passportNo; }
    public void setPassportNo(String passportNo) { this.passportNo = passportNo; }

    public LocalDate getPassportValidTill() { return passportValidTill; }
    public void setPassportValidTill(LocalDate passportValidTill) { this.passportValidTill = passportValidTill; }

    public Long getDipNumber() { return dipNumber; }
    public void setDipNumber(Long dipNumber) { this.dipNumber = dipNumber; }

    public LocalDate getDipValidTill() { return dipValidTill; }
    public void setDipValidTill(LocalDate dipValidTill) { this.dipValidTill = dipValidTill; }
}