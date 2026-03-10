package com.tradesim.tradesimulator.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class NotificationConfig {

    @Bean
    @Primary  // tells Spring to use this when injecting NotificationService
    public NotificationService notificationService() {
        return new DashboardNotificationDecorator(
            new ConsoleNotificationService()
        );
    }
}