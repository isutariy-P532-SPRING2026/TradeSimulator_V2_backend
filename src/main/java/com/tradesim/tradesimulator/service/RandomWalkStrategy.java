package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Stock;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class RandomWalkStrategy implements PriceUpdateStrategy {
    @Override
    public double calculateNewPrice(Stock stock, Random random) {
        // Arrange: get current price
        double current = stock.getCurrentPrice();
        // Act: apply random ±2% change
        double changePercent = (random.nextDouble() * 4) - 2;
        // Return new price
        return current * (1 + changePercent / 100);
    }
}