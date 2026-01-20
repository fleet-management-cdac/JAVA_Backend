package com.example.dto;

import java.time.LocalDate;

public class UserProfileDTO {
    private Long userDetailsId;
    private Long userId;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
    private String address;
    private String cityName;
    private String stateName;
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
    public Long getUserDetailsId() { return userDetailsId; }
    public void setUserDetailsId(Long userDetailsId) { this.userDetailsId = userDetailsId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public String getStateName() { return stateName; }
    public void setStateName(String stateName) { this.stateName = stateName; }

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