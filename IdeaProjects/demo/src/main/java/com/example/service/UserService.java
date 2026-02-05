package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.CreateStaffRequestDTO;
import com.example.dto.StaffResponseDTO;
import com.example.dto.UpdateUserDetailsDTO;
import com.example.dto.UserProfileDTO;
import com.example.entity.CityMaster;
import com.example.entity.UserAuth;
import com.example.entity.UserDetail;
import com.example.repository.CityMasterRepository;
import com.example.repository.HubMasterRepository;
import com.example.repository.UserAuthRepository;
import com.example.repository.UserDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserService {

    @Autowired
    private UserDetailRepository userDetailRepository;
    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private CityMasterRepository cityMasterRepository;

    @Autowired
    private HubMasterRepository hubMasterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ApiResponseDTO<UserProfileDTO> getUserDetailsByUserId(Long userId) {

        var userDetailOpt = userDetailRepository.findByUserIdWithDetails(userId);

        if (userDetailOpt.isPresent()) {
            UserDetail ud = userDetailOpt.get();
            UserProfileDTO dto = mapToDTO(ud);
            return ApiResponseDTO.success("User details fetched", dto);
        }

        // Fallback: If user details don't exist, check if UserAuth exists (e.g. new
        // user)
        var userAuthOpt = userAuthRepository.findById(userId);
        if (userAuthOpt.isPresent()) {
            var user = userAuthOpt.get();
            UserProfileDTO dto = new UserProfileDTO();
            dto.setUserId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());
            // Return empty profile with basic auth info
            return ApiResponseDTO.success("User found (profile incomplete)", dto);
        }

        return ApiResponseDTO.error("User not found");
    }

    private UserProfileDTO mapToDTO(UserDetail ud) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserDetailsId(ud.getId());

        // From UserAuth
        if (ud.getUser() != null) {
            dto.setUserId(ud.getUser().getId());
            dto.setEmail(ud.getUser().getEmail());
            dto.setRole(ud.getUser().getRole());
        }

        // From UserDetail
        dto.setFirstName(ud.getFirstName());
        dto.setLastName(ud.getLastName());
        dto.setAddress(ud.getAddress());
        dto.setZipcode(ud.getZipcode());
        dto.setPhoneHome(ud.getPhoneHome());
        dto.setPhoneCell(ud.getPhoneCell());
        dto.setDateOfBirth(ud.getDateOfBirth());
        dto.setDrivingLicenseNo(ud.getDrivingLicenseNo());
        dto.setLicenseValidTill(ud.getLicenseValidTill());
        dto.setPassportNo(ud.getPassportNo());
        dto.setPassportValidTill(ud.getPassportValidTill());
        dto.setDipNumber(ud.getDipNumber());
        dto.setDipValidTill(ud.getDipValidTill());

        // From City/State
        if (ud.getCity() != null) {
            dto.setCityName(ud.getCity().getCityName());
            if (ud.getCity().getState() != null) {
                dto.setStateName(ud.getCity().getState().getStateName());
            }
        }

        // From Hub
        if (ud.getAssignedHub() != null) {
            dto.setHubName(ud.getAssignedHub().getHubName());
        }

        return dto;
    }

    @Transactional
    public ApiResponseDTO<UserProfileDTO> updateUserDetails(Long userId, UpdateUserDetailsDTO request) {

        // Find existing user details by userId
        var userDetailOpt = userDetailRepository.findByUserId(userId);

        UserDetail ud;
        if (userDetailOpt.isPresent()) {
            ud = userDetailOpt.get();
        } else {
            // Create new user details if not exists (for OAuth users)
            var userAuthOpt = userAuthRepository.findById(userId);
            if (userAuthOpt.isEmpty()) {
                return ApiResponseDTO.error("User not found");
            }
            ud = new UserDetail();
            ud.setUser(userAuthOpt.get());
        }

        // Update fields
        if (request.getFirstName() != null) {
            ud.setFirstName(request.getFirstName());
        }
        ud.setLastName(request.getLastName());
        ud.setAddress(request.getAddress());
        ud.setZipcode(request.getZipcode());
        ud.setPhoneHome(request.getPhoneHome());
        ud.setPhoneCell(request.getPhoneCell());
        ud.setDateOfBirth(request.getDateOfBirth());
        ud.setDrivingLicenseNo(request.getDrivingLicenseNo());
        ud.setLicenseValidTill(request.getLicenseValidTill());
        ud.setPassportNo(request.getPassportNo());
        ud.setPassportValidTill(request.getPassportValidTill());
        ud.setDipNumber(request.getDipNumber());
        ud.setDipValidTill(request.getDipValidTill());

        // Set city if provided
        if (request.getCityId() != null) {
            CityMaster city = cityMasterRepository.findById(request.getCityId()).orElse(null);
            ud.setCity(city);
        } else {
            ud.setCity(null);
        }

        // Save
        ud = userDetailRepository.save(ud);

        // Build response DTO
        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserDetailsId(ud.getId());

        if (ud.getUser() != null) {
            dto.setUserId(ud.getUser().getId());
            dto.setEmail(ud.getUser().getEmail());
            dto.setRole(ud.getUser().getRole());
        }

        dto.setFirstName(ud.getFirstName());
        dto.setLastName(ud.getLastName());
        dto.setAddress(ud.getAddress());
        dto.setZipcode(ud.getZipcode());
        dto.setPhoneHome(ud.getPhoneHome());
        dto.setPhoneCell(ud.getPhoneCell());
        dto.setDateOfBirth(ud.getDateOfBirth());
        dto.setDrivingLicenseNo(ud.getDrivingLicenseNo());
        dto.setLicenseValidTill(ud.getLicenseValidTill());
        dto.setPassportNo(ud.getPassportNo());
        dto.setPassportValidTill(ud.getPassportValidTill());
        dto.setDipNumber(ud.getDipNumber());
        dto.setDipValidTill(ud.getDipValidTill());

        if (ud.getCity() != null) {
            dto.setCityName(ud.getCity().getCityName());
            if (ud.getCity().getState() != null) {
                dto.setStateName(ud.getCity().getState().getStateName());
            }
        }

        return ApiResponseDTO.success("User details updated successfully", dto);
    }

    // ========== ADMIN: ASSIGN HUB TO STAFF ==========
    @Transactional
    public ApiResponseDTO<String> assignHubToUser(Long userId, Long hubId) {
        var userDetailOpt = userDetailRepository.findByUserId(userId);
        if (userDetailOpt.isEmpty()) {
            return ApiResponseDTO.error("User details not found");
        }

        var hubOpt = hubMasterRepository.findById(hubId);
        if (hubOpt.isEmpty()) {
            return ApiResponseDTO.error("Hub not found");
        }

        UserDetail ud = userDetailOpt.get();
        ud.setAssignedHub(hubOpt.get());
        userDetailRepository.save(ud);

        return ApiResponseDTO.success("Hub assigned to user successfully",
                "User " + userId + " assigned to hub: " + hubOpt.get().getHubName());
    }

    // ========== ADMIN: CREATE STAFF ==========
    @Transactional
    public ApiResponseDTO<StaffResponseDTO> createStaff(CreateStaffRequestDTO request) {
        // Validate required fields
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return ApiResponseDTO.error("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            return ApiResponseDTO.error("Password must be at least 6 characters");
        }
        if (request.getFirstName() == null || request.getFirstName().isBlank()) {
            return ApiResponseDTO.error("First name is required");
        }

        // Check if email already exists
        if (userAuthRepository.existsByEmail(request.getEmail())) {
            return ApiResponseDTO.error("Email already registered");
        }

        // Create UserAuth with role 'staff'
        UserAuth userAuth = new UserAuth();
        userAuth.setEmail(request.getEmail());
        userAuth.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userAuth.setRole("staff");
        userAuth.setCreatedAt(Instant.now());
        userAuth = userAuthRepository.save(userAuth);

        // Create UserDetail
        UserDetail userDetail = new UserDetail();
        userDetail.setUser(userAuth);
        userDetail.setFirstName(request.getFirstName());
        userDetail.setLastName(request.getLastName());
        userDetail.setPhoneCell(request.getPhoneCell());

        // Assign hub if provided
        if (request.getHubId() != null) {
            var hubOpt = hubMasterRepository.findById(request.getHubId());
            hubOpt.ifPresent(userDetail::setAssignedHub);
        }

        userDetail = userDetailRepository.save(userDetail);

        // Build response
        StaffResponseDTO dto = mapToStaffDTO(userAuth, userDetail);
        return ApiResponseDTO.success("Staff created successfully", dto);
    }

    // ========== ADMIN: GET ALL STAFF ==========
    public ApiResponseDTO<java.util.List<StaffResponseDTO>> getAllStaff() {
        java.util.List<UserAuth> staffUsers = userAuthRepository.findByRole("staff");
        java.util.List<StaffResponseDTO> staffList = new java.util.ArrayList<>();

        for (UserAuth auth : staffUsers) {
            var detailOpt = userDetailRepository.findByUserIdWithHub(auth.getId());
            UserDetail detail = detailOpt.orElse(null);
            staffList.add(mapToStaffDTO(auth, detail));
        }

        return ApiResponseDTO.success("Staff list fetched", staffList);
    }

    // ========== ADMIN: DELETE STAFF ==========
    @Transactional
    public ApiResponseDTO<String> deleteStaff(Long userId) {
        var userAuthOpt = userAuthRepository.findById(userId);
        if (userAuthOpt.isEmpty()) {
            return ApiResponseDTO.error("User not found");
        }

        UserAuth userAuth = userAuthOpt.get();
        if (!"staff".equals(userAuth.getRole())) {
            return ApiResponseDTO.error("User is not a staff member");
        }

        // Delete user detail first (if exists)
        var userDetailOpt = userDetailRepository.findByUserId(userId);
        userDetailOpt.ifPresent(userDetailRepository::delete);

        // Delete user auth
        userAuthRepository.delete(userAuth);

        return ApiResponseDTO.success("Staff deleted successfully", "User " + userId + " has been removed");
    }

    // ========== HELPER: Map to StaffResponseDTO ==========
    private StaffResponseDTO mapToStaffDTO(UserAuth auth, UserDetail detail) {
        StaffResponseDTO dto = new StaffResponseDTO();
        dto.setUserId(auth.getId());
        dto.setEmail(auth.getEmail());
        dto.setCreatedAt(auth.getCreatedAt());

        if (detail != null) {
            dto.setFirstName(detail.getFirstName());
            dto.setLastName(detail.getLastName());
            dto.setPhoneCell(detail.getPhoneCell());
            if (detail.getAssignedHub() != null) {
                dto.setHubId(detail.getAssignedHub().getId());
                dto.setHubName(detail.getAssignedHub().getHubName());
            }
        }
        return dto;
    }
}