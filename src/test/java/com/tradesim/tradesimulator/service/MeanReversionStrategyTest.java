package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Stock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MeanReversionStrategyTest {

    @Test
    void priceMovesCloserToMeanAfterExtremeDeviation() {
        // Arrange
        MeanReversionStrategy strategy = new MeanReversionStrategy();
        Stock stock = new Stock("AAPL", 100.0);
        java.util.Random seeded = new java.util.Random(0);

        // Seed window with 9 values near 100, then spike to 200
        for (int i = 0; i < 9; i++) {
            strategy.calculateNewPrice(stock, seeded);
        }
        stock.setCurrentPrice(200.0);

        // Act — next price should be pulled below 200
        double result = strategy.calculateNewPrice(stock, seeded);

        // Assert
        assertTrue(result < 200.0, "Mean reversion should pull price below 200");
    }
}