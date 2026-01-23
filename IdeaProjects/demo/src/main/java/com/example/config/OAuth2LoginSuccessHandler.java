package com.example.config;

import com.example.entity.UserAuth;
import com.example.entity.UserDetail;
import com.example.repository.UserAuthRepository;
import com.example.repository.UserDetailRepository;
import com.example.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User googleUser = (OAuth2User) authentication.getPrincipal();
        String email = googleUser.getAttribute("email");

        // Fetch User
        UserAuth userAuth = userAuthRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetail userDetail = userDetailRepository.findByUser(userAuth)
                .orElseThrow(() -> new RuntimeException("Details not found"));

        // Check License
        boolean hasLicense = userDetail.getDrivingLicenseNo() != null && !userDetail.getDrivingLicenseNo().trim().isEmpty();

        String targetUrl;
        if (hasLicense) {
            // Dashboard Redirect
            String accessToken = jwtUtil.generateToken(userAuth.getId(), userAuth.getEmail(), userAuth.getRole());
            targetUrl = frontendUrl + "/oauth2/redirect?token=" + accessToken + "&status=success";
        } else {
            // Profile Completion Redirect
            String tempToken = jwtUtil.generateToken(userAuth.getId(), userAuth.getEmail(), "PRE_VERIFICATION");
            targetUrl = frontendUrl + "/complete-profile?token=" + tempToken + "&status=incomplete";
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}