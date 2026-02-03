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

    @Column(name = "address")
    private String address;

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

    @Column(name = "license_valid_till")
    private LocalDate licenseValidTill;

    @Column(name = "passport_no", length = 100)
    private String passportNo;

    @Column(name = "passport_valid_till")
    private LocalDate passportValidTill;

    @Column(name = "dip_number")
    private Long dipNumber;

    @Column(name = "dip_valid_till")
    private LocalDate dipValidTill;

    // Hub assignment for staff members
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id")
    private HubMaster assignedHub;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public LocalDate getPassportValidTill() {
        return passportValidTill;
    }

    public void setPassportValidTill(LocalDate passportValidTill) {
        this.passportValidTill = passportValidTill;
    }

    public Long getDipNumber() {
        return dipNumber;
    }

    public void setDipNumber(Long dipNumber) {
        this.dipNumber = dipNumber;
    }

    public LocalDate getDipValidTill() {
        return dipValidTill;
    }

    public void setDipValidTill(LocalDate dipValidTill) {
        this.dipValidTill = dipValidTill;
    }

    public HubMaster getAssignedHub() {
        return assignedHub;
    }

    public void setAssignedHub(HubMaster assignedHub) {
        this.assignedHub = assignedHub;
    }

}