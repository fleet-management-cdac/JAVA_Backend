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
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

// === ADDED LOGGING ===
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        logger.info("=================================================");
        logger.info("🚀 STARTED: OAuth2 User Load Request");

        try {
            // Load user from Google
            OAuth2User oAuth2User = super.loadUser(userRequest);
            logger.info("✅ SUCCESS: Google User Loaded. Attributes: {}", oAuth2User.getAttributes());

            return processOAuth2User(userRequest, oAuth2User);

        } catch (Exception ex) {
            logger.error("❌ ERROR: Failed to load OAuth2 user. Reason: {}", ex.getMessage());
            ex.printStackTrace();
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        logger.info("🔍 Processing user with email: {}", email);

        if (email == null || email.isEmpty()) {
            logger.error("❌ ERROR: Email not found in OAuth2 provider response");
            throw new RuntimeException("Email not found from OAuth2 provider");
        }

        Optional<UserAuth> userOptional = userAuthRepository.findByEmail(email);
        UserAuth user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            logger.info("✨ USER FOUND in Database! ID: {}", user.getId());
            logger.info("   Current Provider: {}", user.getProvider());

            if (!UserAuth.AuthProvider.GOOGLE.equals(user.getProvider())) {
                logger.info("🔄 UPDATING provider to GOOGLE (Account Linking)");
                user.setProvider(UserAuth.AuthProvider.GOOGLE);
                userAuthRepository.save(user);
            }
        } else {
            logger.warn("⚠️ USER NOT FOUND. Starting Auto-Registration...");

            // Register new user
            user = new UserAuth();
            user.setEmail(email);
            user.setProvider(UserAuth.AuthProvider.GOOGLE);
            user.setRole("customer");
            user.setCreatedAt(Instant.now());
            // No password for OAuth users
            user = userAuthRepository.save(user);
            logger.info("✅ NEW USER SAVED with ID: {}", user.getId());

            // Create details
            UserDetail userDetail = new UserDetail();
            userDetail.setUser(user);

            String name = oAuth2User.getAttribute("name");
            logger.info("   Extracted Name: {}", name);

            if (name != null) {
                String[] parts = name.split(" ");
                userDetail.setFirstName(parts[0]);
                if (parts.length > 1) {
                    userDetail.setLastName(parts[parts.length - 1]);
                }
            } else {
                userDetail.setFirstName("OAuth User");
            }
            userDetailRepository.save(userDetail);
            logger.info("✅ User Details Saved.");
        }

        logger.info("=================================================");
        return oAuth2User;
    }
}
