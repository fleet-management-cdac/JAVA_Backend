package com.example.service;

import com.example.dto.*;
import com.example.entity.CityMaster;
import com.example.entity.UserAuth;
import com.example.entity.UserDetail;
import com.example.repository.CityMasterRepository;
import com.example.repository.UserAuthRepository;
import com.example.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.util.JwtUtil;
import java.time.Instant;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private CityMasterRepository cityMasterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public ApiResponseDTO<LoginResponseDTO> login(LoginRequestDTO request) {

        // Validate input
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return ApiResponseDTO.error("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            return ApiResponseDTO.error("Password is required");
        }
        // Find user by email
        Optional<UserAuth> userOpt = userAuthRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return ApiResponseDTO.error("Invalid email or password");
        }
        UserAuth user = userOpt.get();
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return ApiResponseDTO.error("Invalid email or password");
        }
        // Get user details for first name
        String firstName = "";
        Optional<UserDetail> detailOpt = userDetailRepository.findByUserId(user.getId());
        if (detailOpt.isPresent()) {
            firstName = detailOpt.get().getFirstName();
        }
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
        // Build response
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(firstName);
        response.setRole(user.getRole());
        return ApiResponseDTO.success("Login successful", response);
    }


    @Transactional
    public ApiResponseDTO<UserResponseDTO> registerUser(RegisterRequestDTO request) {

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

        // Create UserAuth
        UserAuth userAuth = new UserAuth();
        userAuth.setEmail(request.getEmail());
        userAuth.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userAuth.setRole("customer");
//        userAuth.setCreatedAt(Instant.now());
        userAuth = userAuthRepository.save(userAuth);

        // Create UserDetail
        UserDetail userDetail = new UserDetail();
        userDetail.setUser(userAuth);
        userDetail.setFirstName(request.getFirstName());
        userDetail.setLastName(request.getLastName());
        userDetail.setAddress(request.getAddress());  // Updated field name
        userDetail.setZipcode(request.getZipcode());
        userDetail.setPhoneHome(request.getPhoneHome());
        userDetail.setPhoneCell(request.getPhoneCell());
        userDetail.setDateOfBirth(request.getDateOfBirth());

        // License Info (simplified)
        userDetail.setDrivingLicenseNo(request.getDrivingLicenseNo());
        userDetail.setLicenseValidTill(request.getLicenseValidTill());

        // Passport Info (simplified)
        userDetail.setPassportNo(request.getPassportNo());
        userDetail.setPassportValidTill(request.getPassportValidTill());

        // DIP Info (simplified)
        userDetail.setDipNumber(request.getDipNumber());
        userDetail.setDipValidTill(request.getDipValidTill());

        // Set City if provided
        if (request.getCityId() != null) {
            CityMaster city = cityMasterRepository.findById(request.getCityId()).orElse(null);
            userDetail.setCity(city);
        }

        userDetailRepository.save(userDetail);

        // Prepare response
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setUserId(userAuth.getId());
        responseDTO.setEmail(userAuth.getEmail());
        responseDTO.setFirstName(userDetail.getFirstName());
        responseDTO.setLastName(userDetail.getLastName());
        responseDTO.setRole(userAuth.getRole());

        return ApiResponseDTO.success("Registration successful", responseDTO);
    }
}