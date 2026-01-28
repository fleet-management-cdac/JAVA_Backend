package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.UpdateUserDetailsDTO;
import com.example.dto.UserProfileDTO;
import com.example.entity.CityMaster;
import com.example.entity.UserDetail;
import com.example.repository.CityMasterRepository;
import com.example.repository.UserAuthRepository;
import com.example.repository.UserDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDetailRepository userDetailRepository;
    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private CityMasterRepository cityMasterRepository;

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
}