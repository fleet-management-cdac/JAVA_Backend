package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.UserProfileDTO;
import com.example.entity.UserDetail;
import com.example.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDetailRepository userDetailRepository;

    public ApiResponseDTO<UserProfileDTO> getUserDetailsById(Long userDetailId) {

        var userDetailOpt = userDetailRepository.findByIdWithDetails(userDetailId);

        if (userDetailOpt.isEmpty()) {
            return ApiResponseDTO.error("User details not found");
        }

        UserDetail ud = userDetailOpt.get();
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

        return ApiResponseDTO.success("User details fetched", dto);
    }
}