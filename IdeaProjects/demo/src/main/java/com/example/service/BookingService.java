package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.BookingRequestDTO;
import com.example.dto.BookingResponseDTO;
import com.example.entity.*;
import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BookingCustomerDetailRepository bookingCustomerDetailRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleRateRepository vehicleRateRepository;

    @Autowired
    private HubMasterRepository hubMasterRepository;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private CityMasterRepository cityMasterRepository;
    @Autowired
    private AddonRepository addonRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    // ========== CREATE BOOKING ==========
    @Transactional
    public ApiResponseDTO<BookingResponseDTO> createBooking(BookingRequestDTO request) {

        // Validate required fields
        if (request.getVehicleId() == null) {
            return ApiResponseDTO.error("Vehicle ID is required");
        }
        if (request.getRateId() == null) {
            return ApiResponseDTO.error("Rate ID is required");
        }
        if (request.getPickupHubId() == null) {
            return ApiResponseDTO.error("Pickup hub is required");
        }
        if (request.getReturnHubId() == null) {
            return ApiResponseDTO.error("Return hub is required");
        }
        if (request.getPickupDatetime() == null || request.getReturnDatetime() == null) {
            return ApiResponseDTO.error("Pickup and return datetime are required");
        }
        if (request.getFirstName() == null || request.getFirstName().isBlank()) {
            return ApiResponseDTO.error("Customer first name is required");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return ApiResponseDTO.error("Customer email is required");
        }

        // Fetch related entities
        // vehicleId in request is actually vehicleTypeId
        VehicleType vehicleType = vehicleTypeRepository.findById(request.getVehicleId()).orElse(null);
        if (vehicleType == null) {
            return ApiResponseDTO.error("Vehicle type not found");
        }

        VehicleRate rate = vehicleRateRepository.findById(request.getRateId()).orElse(null);
        if (rate == null) {
            return ApiResponseDTO.error("Rate not found");
        }

        HubMaster pickupHub = hubMasterRepository.findById(request.getPickupHubId()).orElse(null);
        HubMaster returnHub = hubMasterRepository.findById(request.getReturnHubId()).orElse(null);
        if (pickupHub == null || returnHub == null) {
            return ApiResponseDTO.error("Invalid hub IDs");
        }

        // Create Booking
        Booking booking = new Booking();

        if (request.getUserId() != null) {
            UserAuth user = userAuthRepository.findById(request.getUserId()).orElse(null);
            booking.setUser(user);
        }

        booking.setVehicleType(vehicleType);
        booking.setRate(rate);
        booking.setPickupHub(pickupHub);
        booking.setReturnHub(returnHub);
        booking.setPickupDatetime(request.getPickupDatetime());
        booking.setReturnDatetime(request.getReturnDatetime());
        booking.setStatus("reserved");
        booking.setCreatedAt(Instant.now());
        // Set addon if provided
        if (request.getAddonId() != null) {
            Addon addon = addonRepository.findById(request.getAddonId()).orElse(null);
            booking.setAddon(addon);
        }

        // Save booking first
        booking = bookingRepository.save(booking);

        // Create BookingCustomerDetail
        BookingCustomerDetail customerDetail = new BookingCustomerDetail();
        customerDetail.setBooking(booking);
        customerDetail.setFirstName(request.getFirstName());
        customerDetail.setLastName(request.getLastName());
        customerDetail.setEmail(request.getEmail());
        customerDetail.setAddress(request.getAddress());
        customerDetail.setZipcode(request.getZipcode());
        customerDetail.setPhoneHome(request.getPhoneHome());
        customerDetail.setPhoneCell(request.getPhoneCell());
        customerDetail.setDateOfBirth(request.getDateOfBirth());
        customerDetail.setDrivingLicenseNo(request.getDrivingLicenseNo());
        customerDetail.setLicenseValidTill(request.getLicenseValidTill());
        customerDetail.setPassportNo(request.getPassportNo());
        customerDetail.setPassportValidTill(request.getPassportValidTill());
        customerDetail.setDipNumber(request.getDipNumber());
        customerDetail.setDipValidTill(request.getDipValidTill());

        if (request.getCityId() != null) {
            CityMaster city = cityMasterRepository.findById(request.getCityId()).orElse(null);
            customerDetail.setCity(city);
        }

        // Save customer detail
        customerDetail = bookingCustomerDetailRepository.save(customerDetail);

        // Note: Vehicle is now assigned during handover, not at booking
        // So we don't update vehicle status here

        // Build response
        BookingResponseDTO response = buildBookingResponse(booking, customerDetail);

        // ========== SEND CONFIRMATION EMAIL ==========
        try {
            emailService.sendBookingConfirmationEmail(
                    request.getEmail(),
                    request.getFirstName() + " " + (request.getLastName() != null ? request.getLastName() : ""),
                    booking.getId(),
                    vehicleType.getTypeName(), // Vehicle type name instead of actual vehicle
                    "To be assigned", // Registration assigned at handover
                    pickupHub.getHubName(),
                    returnHub.getHubName(),
                    booking.getPickupDatetime(),
                    booking.getReturnDatetime(),
                    request.getAddress(),
                    request.getPhoneCell());
            System.out.println("✅ Confirmation email sent to: " + request.getEmail());
        } catch (Exception e) {
            System.err.println("⚠️ Failed to send confirmation email: " + e.getMessage());
        }

        return ApiResponseDTO.success("Booking created successfully", response);
    }

    // ========== GET BOOKING BY ID ==========
    public ApiResponseDTO<BookingResponseDTO> getBookingById(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findByIdWithDetails(bookingId);

        if (bookingOpt.isEmpty()) {
            return ApiResponseDTO.error("Booking not found");
        }

        Booking booking = bookingOpt.get();
        BookingResponseDTO response = buildBookingResponse(booking, booking.getBookingCustomerDetail());

        return ApiResponseDTO.success("Booking fetched", response);
    }

    // ========== GET ALL BOOKINGS ==========
    public ApiResponseDTO<List<BookingResponseDTO>> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingResponseDTO> responses = new ArrayList<>();

        for (Booking booking : bookings) {
            responses.add(buildBookingResponse(booking, booking.getBookingCustomerDetail()));
        }

        return ApiResponseDTO.success("Bookings fetched", responses);
    }

    // ========== GET BOOKINGS BY USER ID ==========
    public ApiResponseDTO<List<BookingResponseDTO>> getBookingsByUserId(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        List<BookingResponseDTO> responses = new ArrayList<>();

        for (Booking booking : bookings) {
            responses.add(buildBookingResponse(booking, booking.getBookingCustomerDetail()));
        }

        return ApiResponseDTO.success("User bookings fetched", responses);
    }

    // ========== GET BOOKINGS BY HUB ID (For Staff Dashboard) ==========
    public ApiResponseDTO<List<BookingResponseDTO>> getBookingsByHubId(Long hubId) {
        List<Booking> bookings = bookingRepository.findByHubId(hubId);
        List<BookingResponseDTO> responses = new ArrayList<>();

        for (Booking booking : bookings) {
            responses.add(buildBookingResponse(booking, booking.getBookingCustomerDetail()));
        }

        return ApiResponseDTO.success("Hub bookings fetched", responses);
    }

    // ========== UPDATE BOOKING STATUS ==========
    @Transactional
    public ApiResponseDTO<BookingResponseDTO> updateBookingStatus(Long bookingId, String status) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);

        if (bookingOpt.isEmpty()) {
            return ApiResponseDTO.error("Booking not found");
        }

        Booking booking = bookingOpt.get();
        booking.setStatus(status);
        booking = bookingRepository.save(booking);

        // Note: Vehicle status is now managed via Handover, not directly on Booking

        return ApiResponseDTO.success("Booking status updated",
                buildBookingResponse(booking, booking.getBookingCustomerDetail()));
    }

    // ========== DELETE BOOKING ==========
    @Transactional
    public ApiResponseDTO<Void> deleteBooking(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);

        if (bookingOpt.isEmpty()) {
            return ApiResponseDTO.error("Booking not found");
        }

        Booking booking = bookingOpt.get();

        if (booking.getBookingCustomerDetail() != null) {
            bookingCustomerDetailRepository.delete(booking.getBookingCustomerDetail());
        }

        // Note: Vehicle status handled separately via Handover

        bookingRepository.delete(booking);

        return ApiResponseDTO.success("Booking deleted", null);
    }

    // ========== HELPER METHOD ==========
    private BookingResponseDTO buildBookingResponse(Booking booking, BookingCustomerDetail cd) {
        BookingResponseDTO response = new BookingResponseDTO();

        response.setBookingId(booking.getId());
        response.setPickupDatetime(booking.getPickupDatetime());
        response.setReturnDatetime(booking.getReturnDatetime());
        response.setStatus(booking.getStatus());
        response.setCreatedAt(booking.getCreatedAt());

        if (booking.getUser() != null) {
            response.setUserId(booking.getUser().getId());
        }

        // Use vehicleType instead of vehicle
        if (booking.getVehicleType() != null) {
            VehicleType vt = booking.getVehicleType();
            response.setVehicleTypeId(vt.getId());
            response.setVehicleTypeName(vt.getTypeName());
            // Vehicle name/registration will be null until handover assigns actual vehicle
        }

        if (booking.getRate() != null) {
            response.setRatePlan(booking.getRate().getPlans());
        }

        if (booking.getPickupHub() != null) {
            response.setPickupHubId(booking.getPickupHub().getId());
            response.setPickupHub(booking.getPickupHub().getHubName());
        }

        if (booking.getReturnHub() != null) {
            response.setReturnHubId(booking.getReturnHub().getId());
            response.setReturnHub(booking.getReturnHub().getHubName());
        }

        if (cd != null) {
            response.setBookingCustomerId(cd.getId());
            response.setFirstName(cd.getFirstName());
            response.setLastName(cd.getLastName());
            response.setEmail(cd.getEmail());
            response.setAddress(cd.getAddress());
            response.setZipcode(cd.getZipcode());
            response.setPhoneHome(cd.getPhoneHome());
            response.setPhoneCell(cd.getPhoneCell());
            response.setDateOfBirth(cd.getDateOfBirth());
            response.setDrivingLicenseNo(cd.getDrivingLicenseNo());
            response.setLicenseValidTill(cd.getLicenseValidTill());
            response.setPassportNo(cd.getPassportNo());
            response.setPassportValidTill(cd.getPassportValidTill());
            response.setDipNumber(cd.getDipNumber());
            response.setDipValidTill(cd.getDipValidTill());

            if (cd.getCity() != null) {
                response.setCityName(cd.getCity().getCityName());
                if (cd.getCity().getState() != null) {
                    response.setStateName(cd.getCity().getState().getStateName());
                }
            }
        }

        return response;
    }
}