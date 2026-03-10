package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Stock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RandomWalkStrategyTest {

    @Test
    void priceStaysWithinTwoPercentBand() {
        // Arrange
        RandomWalkStrategy strategy = new RandomWalkStrategy();
        Stock stock = new Stock("AAPL", 100.0);
        java.util.Random seeded = new java.util.Random(42); // deterministic

        // Act + Assert — run 100 iterations, price must stay within ±2%
        for (int i = 0; i < 100; i++) {
            double before = stock.getCurrentPrice();
            double after  = strategy.calculateNewPrice(stock, seeded);
            double changePct = Math.abs((after - before) / before * 100);
            assertTrue(changePct <= 2.0,
                "Price changed by " + changePct + "% which exceeds ±2% band");
            stock.setCurrentPrice(after);
        }
    }
}