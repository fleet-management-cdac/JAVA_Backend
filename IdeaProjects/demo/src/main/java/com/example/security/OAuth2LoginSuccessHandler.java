package com.example.security;

import com.example.entity.UserAuth;
import com.example.repository.UserAuthRepository;
import com.example.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

// === ADDED LOGGING ===
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        logger.info("=================================================");
        logger.info("🎉 LOGIN SUCCESS: OAuth2 Authentication Successful!");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        logger.info("📧 User Email from Context: {}", email);

        // Fetch user to get ID and Role
        Optional<UserAuth> userOptional = userAuthRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserAuth user = userOptional.get();
            try {
                String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
                logger.info("🔑 JWT Generated Successfully!");
                logger.debug("   Token: {}...", token.substring(0, 15)); // Log partial token for safety

                // Redirect to frontend with token
                String targetUrl = "http://localhost:3000/login/success?token=" + token;
                logger.info("🚀 Redirecting User to: {}", targetUrl);

                getRedirectStrategy().sendRedirect(request, response, targetUrl);

            } catch (Exception e) {
                logger.error("❌ ERROR generating token or redirecting: {}", e.getMessage());
                e.printStackTrace();
            }
        } else {
            logger.error("🚨 CRITICAL ERROR: User authenticated but not found in DB! (Should be auto-registered)");
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
