package com.example.security;

import com.example.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                if (jwtUtil.isTokenValid(token) && !jwtUtil.isTokenExpired(token)) {
                    String email = jwtUtil.extractEmail(token);
                    Long userId = jwtUtil.extractUserId(token);
                    String role = jwtUtil.extractRole(token);
                    Long hubId = jwtUtil.extractHubId(token);

                    // Create authentication with ROLE_ prefix (Spring Security requirement)
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userId, // Principal
                            null, // Credentials
                            Collections.singletonList(
                                    new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));

                    // Store additional details for easy access
                    authentication.setDetails(new JwtUserDetails(userId, email, role, hubId));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                logger.warn("Invalid JWT token: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    // Helper class to store user details
    public static class JwtUserDetails {
        private final Long userId;
        private final String email;
        private final String role;
        private final Long hubId;

        public JwtUserDetails(Long userId, String email, String role, Long hubId) {
            this.userId = userId;
            this.email = email;
            this.role = role;
            this.hubId = hubId;
        }

        public Long getUserId() {
            return userId;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }

        public Long getHubId() {
            return hubId;
        }
    }
}