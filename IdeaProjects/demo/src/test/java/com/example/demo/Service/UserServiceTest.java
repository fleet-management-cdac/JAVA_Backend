package com.example.demo.Service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.UpdateUserDetailsDTO;
import com.example.dto.UserProfileDTO;
import com.example.service.UserService;
import com.example.entity.CityMaster;
import com.example.entity.StateMaster;
import com.example.entity.UserAuth;
import com.example.entity.UserDetail;
import com.example.repository.CityMasterRepository;
import com.example.repository.UserAuthRepository;
import com.example.repository.UserDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDetailRepository userDetailRepository;

    @Mock
    private UserAuthRepository userAuthRepository;

    @Mock
    private CityMasterRepository cityMasterRepository;

    @InjectMocks
    private UserService userService;

    private UserDetail sampleUserDetail;
    private UserAuth sampleUserAuth;

    @BeforeEach
    void setUp() {
        sampleUserAuth = new UserAuth();
        sampleUserAuth.setId(1L);
        sampleUserAuth.setEmail("test@example.com");
        sampleUserAuth.setRole("USER");

        sampleUserDetail = new UserDetail();
        sampleUserDetail.setId(100L);
        sampleUserDetail.setUser(sampleUserAuth);
        sampleUserDetail.setFirstName("John");
        sampleUserDetail.setLastName("Doe");
    }

    // --- Tests for getUserDetailsByUserId ---

    @Test
    @DisplayName("Should return user details when UserID exists")
    void getUserDetailsByUserId_Success() {
        // Arrange
        // Note: Repository now queried by UserId (1L), not DetailId (100L)
        when(userDetailRepository.findByUserIdWithDetails(1L)).thenReturn(Optional.of(sampleUserDetail));

        // Act
        ApiResponseDTO<UserProfileDTO> response = userService.getUserDetailsByUserId(1L);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("User details fetched", response.getMessage());
        assertEquals("John", response.getData().getFirstName());
        assertEquals("test@example.com", response.getData().getEmail());
    }

    @Test
    @DisplayName("Should return partial details when UserAuth exists but UserDetail missing")
    void getUserDetailsByUserId_Fallback() {
        // Arrange
        when(userDetailRepository.findByUserIdWithDetails(1L)).thenReturn(Optional.empty());
        when(userAuthRepository.findById(1L)).thenReturn(Optional.of(sampleUserAuth));

        // Act
        ApiResponseDTO<UserProfileDTO> response = userService.getUserDetailsByUserId(1L);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("User found (profile incomplete)", response.getMessage());
        assertEquals("test@example.com", response.getData().getEmail());
        assertNull(response.getData().getFirstName()); // First name should be null
    }

    @Test
    @DisplayName("Should return error when user does not exist")
    void getUserDetailsByUserId_NotFound() {
        // Arrange
        when(userDetailRepository.findByUserIdWithDetails(99L)).thenReturn(Optional.empty());
        when(userAuthRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        ApiResponseDTO<UserProfileDTO> response = userService.getUserDetailsByUserId(99L);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("User not found", response.getMessage());
    }

    // --- Tests for updateUserDetails ---

    @Test
    @DisplayName("Should update existing user details successfully")
    void updateUserDetails_ExistingUser() {
        // Arrange
        UpdateUserDetailsDTO updateDTO = new UpdateUserDetailsDTO();
        updateDTO.setFirstName("Jane");
        updateDTO.setLastName("Smith");
        updateDTO.setCityId(5L);

        CityMaster mockCity = new CityMaster();
        mockCity.setCityName("New York");
        StateMaster mockState = new StateMaster();
        mockState.setStateName("NY");
        mockCity.setState(mockState);

        when(userDetailRepository.findByUserId(1L)).thenReturn(Optional.of(sampleUserDetail));
        when(cityMasterRepository.findById(5L)).thenReturn(Optional.of(mockCity));
        when(userDetailRepository.save(any(UserDetail.class))).thenReturn(sampleUserDetail);

        // Act
        ApiResponseDTO<UserProfileDTO> response = userService.updateUserDetails(1L, updateDTO);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Jane", sampleUserDetail.getFirstName()); // Verify the entity was updated
        assertEquals("New York", response.getData().getCityName());
        verify(userDetailRepository, times(1)).save(sampleUserDetail);
    }

    @Test
    @DisplayName("Should create new UserDetail if only UserAuth exists")
    void updateUserDetails_CreateNewDetails() {
        // Arrange
        UpdateUserDetailsDTO updateDTO = new UpdateUserDetailsDTO();
        updateDTO.setFirstName("New");
        updateDTO.setLastName("User");

        when(userDetailRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(userAuthRepository.findById(1L)).thenReturn(Optional.of(sampleUserAuth));
        when(userDetailRepository.save(any(UserDetail.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        ApiResponseDTO<UserProfileDTO> response = userService.updateUserDetails(1L, updateDTO);

        // Assert
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        verify(userDetailRepository).save(any(UserDetail.class));
    }

    @Test
    @DisplayName("Should return error if neither UserDetail nor UserAuth is found")
    void updateUserDetails_UserNotFound() {
        // Arrange
        when(userDetailRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(userAuthRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ApiResponseDTO<UserProfileDTO> response = userService.updateUserDetails(1L, new UpdateUserDetailsDTO());

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("User not found", response.getMessage());
    }
}