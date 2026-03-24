package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Stock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrendFollowingStrategyTest {

    @Test
    void upwardMomentumProducesHigherPrice() {
        // Arrange
        TrendFollowingStrategy strategy = new TrendFollowingStrategy();
        Stock stock = new Stock("AAPL", 110.0);
        stock.setCurrentPrice(115.0);         // previous=110, current=115 → upward trend
        java.util.Random seeded = new java.util.Random(1);

        // Act — average of many runs should continue upward
        double sum = 0;
        for (int i = 0; i < 50; i++) sum += strategy.calculateNewPrice(stock, seeded);
        double avg = sum / 50;

        // Assert
        assertTrue(avg > 115.0, "Trend following should continue upward momentum");
    }
}