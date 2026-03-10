package com.tradesim.tradesimulator.factory;

import com.tradesim.tradesimulator.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderFactoryTest {

    private OrderFactory factory;

    @BeforeEach
    void setUp() {
        // Arrange
        factory = new OrderFactory();
    }

    @Test
    void shouldCreateMarketOrder() {
        // Act
        Order order = factory.createOrder("AAPL", 5, "BUY", "MARKET", 0);
        // Assert
        assertEquals(OrderType.MARKET, order.getOrderType());
        assertEquals(OrderSide.BUY, order.getSide());
    }

    @Test
    void shouldCreateLimitOrder() {
        // Act
        Order order = factory.createOrder("TSLA", 3, "SELL", "LIMIT", 250.0);
        // Assert
        assertEquals(OrderType.LIMIT, order.getOrderType());
        assertEquals(250.0, order.getLimitPrice());
    }

    @Test
    void shouldThrowOnInvalidSide() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
            factory.createOrder("AAPL", 1, "MARKKET", "MARKET", 0));
    }

    @Test
    void shouldThrowOnInvalidOrderType() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
            factory.createOrder("AAPL", 1, "BUY", "UNKNOWN", 0));
    }
}