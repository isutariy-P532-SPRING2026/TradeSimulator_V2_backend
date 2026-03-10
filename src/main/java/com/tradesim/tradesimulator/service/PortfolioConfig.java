package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Portfolio;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PortfolioConfig {

    @Bean
    public Portfolio portfolio() {
        return new Portfolio();
    }
}