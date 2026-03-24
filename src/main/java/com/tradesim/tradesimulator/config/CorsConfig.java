package com.tradesim.tradesimulator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins(
                "https://isutariy-p532-spring2026.github.io",
                "http://localhost:8080"
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}