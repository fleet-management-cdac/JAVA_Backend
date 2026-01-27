package com.example; // This might be com.example.demo based on your project

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // --- ADD THIS PART ---
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}