package com.example.config;

import com.example.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF/CORS for development (simpler testing)
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())

                // 2. State Management: Stateless (because we use JWTs)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Define Public vs Private URLs
                .authorizeHttpRequests(auth -> auth
                        // Allow Google Login & Redirects
                        .requestMatchers("/login/**", "/oauth2/**").permitAll()
                        // Public APIs (Matches your existing setup)
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/vehicles/**").permitAll()
                        .requestMatchers("/api/catalog/**").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/api/bookings/**").permitAll()
                        .requestMatchers("/api/locations/**").permitAll()
                        // Everything else requires a Token
                        .anyRequest().authenticated()
                )

                // 4. THE SSO WIRING (This connects your new code)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // Uses your "Engine"
                        )
                        .successHandler(oAuth2LoginSuccessHandler)     // Uses your "Traffic Controller"
                );

        return http.build();
    }
}