package com.example.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_details")
public class UserDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_details_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAuth user;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "address_line1")
    private String addressLine1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private CityMaster city;

    @Column(name = "zipcode", length = 20)
    private String zipcode;

    @Column(name = "phone_home", length = 30)
    private String phoneHome;

    @Column(name = "phone_cell", length = 30)
    private String phoneCell;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "driving_license_no", length = 100)
    private String drivingLicenseNo;

    @Column(name = "license_issued_by", length = 100)
    private String licenseIssuedBy;

    @Column(name = "license_valid_till")
    private LocalDate licenseValidTill;

    @Column(name = "passport_no", length = 100)
    private String passportNo;

    @Column(name = "passport_issued_by", length = 100)
    private String passportIssuedBy;

    @Column(name = "passport_valid_till")
    private LocalDate passportValidTill;

    @Column(name = "vehicle_type_id")
    private Long vehicleTypeId;

    @Column(name = "dip_number")
    private Long dipNumber;

    @Column(name = "dip_issued_by", length = 100)
    private String dipIssuedBy;

    @Column(name = "dip_valid_till")
    private LocalDate dipValidTill;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserAuth getUser() {
        return user;
    }

    public void setUser(UserAuth user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public CityMaster getCity() {
        return city;
    }

    public void setCity(CityMaster city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhoneHome() {
        return phoneHome;
    }

    public void setPhoneHome(String phoneHome) {
        this.phoneHome = phoneHome;
    }

    public String getPhoneCell() {
        return phoneCell;
    }

    public void setPhoneCell(String phoneCell) {
        this.phoneCell = phoneCell;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDrivingLicenseNo() {
        return drivingLicenseNo;
    }

    public void setDrivingLicenseNo(String drivingLicenseNo) {
        this.drivingLicenseNo = drivingLicenseNo;
    }

    public String getLicenseIssuedBy() {
        return licenseIssuedBy;
    }

    public void setLicenseIssuedBy(String licenseIssuedBy) {
        this.licenseIssuedBy = licenseIssuedBy;
    }

    public LocalDate getLicenseValidTill() {
        return licenseValidTill;
    }

    public void setLicenseValidTill(LocalDate licenseValidTill) {
        this.licenseValidTill = licenseValidTill;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getPassportIssuedBy() {
        return passportIssuedBy;
    }

    public void setPassportIssuedBy(String passportIssuedBy) {
        this.passportIssuedBy = passportIssuedBy;
    }

    public LocalDate getPassportValidTill() {
        return passportValidTill;
    }

    public void setPassportValidTill(LocalDate passportValidTill) {
        this.passportValidTill = passportValidTill;
    }

    public Long getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(Long vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public Long getDipNumber() {
        return dipNumber;
    }

    public void setDipNumber(Long dipNumber) {
        this.dipNumber = dipNumber;
    }

    public String getDipIssuedBy() {
        return dipIssuedBy;
    }

    public void setDipIssuedBy(String dipIssuedBy) {
        this.dipIssuedBy = dipIssuedBy;
    }

    public LocalDate getDipValidTill() {
        return dipValidTill;
    }

    public void setDipValidTill(LocalDate dipValidTill) {
        this.dipValidTill = dipValidTill;
    }

}