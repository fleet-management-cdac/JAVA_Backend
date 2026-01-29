package com.example.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    // Define allowed origins (normalized to lowercase for safe comparison)
    private static final Set<String> ALLOWED_ORIGINS = Set.of(
            "https://frontendfleeman01.vercel.app",
            "http://localhost:3000"
    ).stream().map(String::toLowerCase).collect(java.util.stream.Collectors.toSet());

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin = request.getHeader("Origin");

        // Only set CORS headers if origin is explicitly allowed
        if (origin != null && ALLOWED_ORIGINS.contains(origin.toLowerCase())) {
            response.setHeader("Access-Control-Allow-Origin", origin); // Echo validated origin
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With"); // Avoid "*" in production
            response.setHeader("Access-Control-Allow-Credentials", "false");
            response.setHeader("Access-Control-Max-Age", "3600");
        }

        // Handle preflight requests AFTER setting headers
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(req, res);
    }
}