package com.example.config;

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable()) // Note: In production, configure this properly!
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/vehicles/**").permitAll() // Allows /api/vehicles/upload
                        .requestMatchers("/api/catalog/**").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/bookings/**").permitAll()
                        // --- Re-add the lines that were lost during the merge ---
                        .requestMatchers("/api/locations/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/api/email/**").permitAll()
                        .requestMatchers("/api/handovers").permitAll()
                        .requestMatchers("/api/handovers/**").permitAll()
                        .requestMatchers("/api/invoices/**").permitAll()
                        // --------------------------------------------------------
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}