package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.HandoverRequestDTO;
import com.example.dto.HandoverResponseDTO;
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
public class HandoverService {

    @Autowired
    private HandoverRepository handoverRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private UserDetailRepository userDetailRepository;

    // ========== CREATE HANDOVER ==========
    @Transactional
    public ApiResponseDTO<HandoverResponseDTO> createHandover(HandoverRequestDTO request) {

        if (request.getBookingId() == null) {
            return ApiResponseDTO.error("Booking ID is required");
        }
        if (request.getProcessedBy() == null) {
            return ApiResponseDTO.error("Processed by (Staff ID) is required");
        }
        if (request.getFuelStatus() == null || request.getFuelStatus().isBlank()) {
            return ApiResponseDTO.error("Fuel status is required");
        }

        // Fetch booking
        Optional<Booking> bookingOpt = bookingRepository.findByIdWithDetails(request.getBookingId());
        if (bookingOpt.isEmpty()) {
            return ApiResponseDTO.error("Booking not found");
        }

        Booking booking = bookingOpt.get();

        // Check booking status
        if (!"reserved".equals(booking.getStatus())) {
            return ApiResponseDTO.error("Booking is not in 'reserved' status");
        }

        // Fetch staff
        Optional<UserAuth> staffOpt = userAuthRepository.findById(request.getProcessedBy());
        if (staffOpt.isEmpty()) {
            return ApiResponseDTO.error("Staff user not found");
        }

        UserAuth staff = staffOpt.get();

        // Create handover
        Handover handover = new Handover();
        handover.setBooking(booking);
        handover.setProcessedBy(staff);
        handover.setFuelStatus(request.getFuelStatus());
        handover.setCreatedAt(Instant.now());

        handover = handoverRepository.save(handover);

        return ApiResponseDTO.success("Handover created successfully", buildResponse(handover));
    }

    // ========== GET HANDOVER BY ID ==========
    public ApiResponseDTO<HandoverResponseDTO> getHandoverById(Long handoverId) {
        Optional<Handover> handoverOpt = handoverRepository.findByIdWithDetails(handoverId);

        if (handoverOpt.isEmpty()) {
            return ApiResponseDTO.error("Handover not found");
        }

        return ApiResponseDTO.success("Handover fetched", buildResponse(handoverOpt.get()));
    }

    // ========== GET HANDOVERS BY BOOKING ID ==========
    public ApiResponseDTO<List<HandoverResponseDTO>> getHandoversByBookingId(Long bookingId) {
        List<Handover> handovers = handoverRepository.findByBookingId(bookingId);
        List<HandoverResponseDTO> responses = new ArrayList<>();

        for (Handover handover : handovers) {
            responses.add(buildResponse(handover));
        }

        return ApiResponseDTO.success("Handovers fetched", responses);
    }

    // ========== Helper ==========
    private HandoverResponseDTO buildResponse(Handover handover) {
        HandoverResponseDTO response = new HandoverResponseDTO();
        response.setHandoverId(handover.getId());
        response.setFuelStatus(handover.getFuelStatus());
        response.setCreatedAt(handover.getCreatedAt());

        if (handover.getBooking() != null) {
            Booking booking = handover.getBooking();
            response.setBookingId(booking.getId());

            if (booking.getVehicle() != null) {
                response.setVehicleName(booking.getVehicle().getCompany() + " " + booking.getVehicle().getModel());
            }

            if (booking.getBookingCustomerDetail() != null) {
                BookingCustomerDetail cd = booking.getBookingCustomerDetail();
                response.setCustomerName(cd.getFirstName() + " " +
                        (cd.getLastName() != null ? cd.getLastName() : ""));
            }
        }

        if (handover.getProcessedBy() != null) {
            Optional<UserDetail> staffDetailOpt = userDetailRepository.findByUserId(handover.getProcessedBy().getId());
            if (staffDetailOpt.isPresent()) {
                UserDetail staffDetail = staffDetailOpt.get();
                response.setProcessedByName(staffDetail.getFirstName() + " " +
                        (staffDetail.getLastName() != null ? staffDetail.getLastName() : ""));
            } else {
                response.setProcessedByName(handover.getProcessedBy().getEmail());
            }
        }

        return response;
    }
}