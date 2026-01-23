package com.example.service;

import com.example.entity.UserAuth;
import com.example.entity.UserDetail;
import com.example.repository.UserAuthRepository;
import com.example.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. Let Spring Security get the user info from Google first
        OAuth2User googleUser = super.loadUser(userRequest);
        
        String email = googleUser.getAttribute("email");
        String firstName = googleUser.getAttribute("given_name");
        String lastName = googleUser.getAttribute("family_name");
        String googleId = googleUser.getAttribute("sub"); // "sub" is Google's unique ID for the user

        // 2. Check if this user is already in our database
        Optional<UserAuth> existingAuth = userAuthRepository.findByEmail(email);

        if (existingAuth.isEmpty()) {
            // --- NEW USER: Create Account ---
            
            // A. Save Login Info (UserAuth)
            UserAuth newUser = new UserAuth();
            newUser.setEmail(email);
            newUser.setAuthProvider(UserAuth.AuthProvider.GOOGLE);
            newUser.setProviderId(googleId);
            newUser.setRole("customer");
            // Password is NULL because they use Google
            UserAuth savedUser = userAuthRepository.save(newUser);

            // B. Save Profile Info (UserDetail)
            UserDetail newDetail = new UserDetail();
            newDetail.setUser(savedUser);
            newDetail.setFirstName(firstName != null ? firstName : "Guest");
            newDetail.setLastName(lastName);
            // License is NULL here. This is what we check later to decide if they need to complete their profile.
            userDetailRepository.save(newDetail);
        }
        
        return googleUser;
    }
}