package com.example.dto;

import java.time.Instant;
import java.time.LocalDate;

public class BookingResponseDTO {
   // Booking fields
   private Long bookingId;
   private Long userId;
   private String vehicleName;
   private String vehicleRegistration;
   private String ratePlan;
   private String pickupHub;
   private String returnHub;
   private Instant pickupDatetime;
   private Instant returnDatetime;
   private String status;
   private Instant createdAt;

   // Customer details fields
   private Long bookingCustomerId;
   private String firstName;
   private String lastName;
   private String email;
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
   public Long getBookingId() { return bookingId; }
   public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

   public Long getUserId() { return userId; }
   public void setUserId(Long userId) { this.userId = userId; }

   public String getVehicleName() { return vehicleName; }
   public void setVehicleName(String vehicleName) { this.vehicleName = vehicleName; }

   public String getVehicleRegistration() { return vehicleRegistration; }
   public void setVehicleRegistration(String vehicleRegistration) { this.vehicleRegistration = vehicleRegistration; }

   public String getRatePlan() { return ratePlan; }
   public void setRatePlan(String ratePlan) { this.ratePlan = ratePlan; }

   public String getPickupHub() { return pickupHub; }
   public void setPickupHub(String pickupHub) { this.pickupHub = pickupHub; }

   public String getReturnHub() { return returnHub; }
   public void setReturnHub(String returnHub) { this.returnHub = returnHub; }

   public Instant getPickupDatetime() { return pickupDatetime; }
   public void setPickupDatetime(Instant pickupDatetime) { this.pickupDatetime = pickupDatetime; }

   public Instant getReturnDatetime() { return returnDatetime; }
   public void setReturnDatetime(Instant returnDatetime) { this.returnDatetime = returnDatetime; }

   public String getStatus() { return status; }
   public void setStatus(String status) { this.status = status; }

   public Instant getCreatedAt() { return createdAt; }
   public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

   public Long getBookingCustomerId() { return bookingCustomerId; }
   public void setBookingCustomerId(Long bookingCustomerId) { this.bookingCustomerId = bookingCustomerId; }

   public String getFirstName() { return firstName; }
   public void setFirstName(String firstName) { this.firstName = firstName; }

   public String getLastName() { return lastName; }
   public void setLastName(String lastName) { this.lastName = lastName; }

   public String getEmail() { return email; }
   public void setEmail(String email) { this.email = email; }

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
