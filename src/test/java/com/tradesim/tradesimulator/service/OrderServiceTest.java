package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MarketFeed marketFeed;

    @Mock
    private NotificationService notificationService;

    private Portfolio portfolio;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        // Arrange
        portfolio = new Portfolio();
        orderService = new OrderService(portfolio, marketFeed, notificationService);
    }

    @Test
    void marketBuyReducesCashAndIncreasesHoldings() {
        // Arrange
        Stock stock = new Stock("AAPL", 100.0);
        when(marketFeed.getStock("AAPL")).thenReturn(stock);
        Order order = new Order("AAPL", 5, OrderSide.BUY, OrderType.MARKET, 0);

        // Act
        orderService.placeOrder(order);

        // Assert
        assertEquals(9500.0, portfolio.getCashBalance(), 0.01);
        assertEquals(5, portfolio.getShares("AAPL"));
    }

    @Test
    void marketSellIncreasesCashAndReducesHoldings() {
        // Arrange
        portfolio.addShares("AAPL", 10);
        portfolio.deductCash(1000.0);
        Stock stock = new Stock("AAPL", 100.0);
        when(marketFeed.getStock("AAPL")).thenReturn(stock);
        Order order = new Order("AAPL", 5, OrderSide.SELL, OrderType.MARKET, 0);

        // Act
        orderService.placeOrder(order);

        // Assert
        assertEquals(9500.0, portfolio.getCashBalance(), 0.01);
        assertEquals(5, portfolio.getShares("AAPL"));
    }

    @Test
    void buyWithInsufficientFundsIsRejected() {
        // Arrange
        Stock stock = new Stock("AAPL", 100.0);
        when(marketFeed.getStock("AAPL")).thenReturn(stock);
        Order order = new Order("AAPL", 999, OrderSide.BUY, OrderType.MARKET, 0);

        // Act
        String result = orderService.placeOrder(order);

        // Assert
        assertEquals("Insufficient funds", result);
        assertEquals(10000.0, portfolio.getCashBalance(), 0.01);
    }

    @Test
    void sellWithInsufficientSharesIsRejected() {
        // Arrange
        Stock stock = new Stock("AAPL", 100.0);
        when(marketFeed.getStock("AAPL")).thenReturn(stock);
        Order order = new Order("AAPL", 5, OrderSide.SELL, OrderType.MARKET, 0);

        // Act
        String result = orderService.placeOrder(order);

        // Assert
        assertEquals("Insufficient shares", result);
    }

    @Test
    void limitBuyExecutesWhenPriceFallsBelowThreshold() {
        // Arrange
        Order order = new Order("AAPL", 1, OrderSide.BUY, OrderType.LIMIT, 150.0);
        orderService.placeOrder(order);
        Stock stock = new Stock("AAPL", 145.0); // below limit

        // Act
        orderService.onPriceUpdate(stock);

        // Assert
        assertEquals(OrderStatus.EXECUTED, order.getStatus());
    }

    @Test
    void limitBuyDoesNotExecuteWhenPriceAboveThreshold() {
        // Arrange
        Order order = new Order("AAPL", 1, OrderSide.BUY, OrderType.LIMIT, 150.0);
        orderService.placeOrder(order);
        Stock stock = new Stock("AAPL", 160.0); // above limit

        // Act
        orderService.onPriceUpdate(stock);

        // Assert
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void limitSellExecutesWhenPriceRisesAboveThreshold() {
        // Arrange
        portfolio.addShares("AAPL", 5);
        Order order = new Order("AAPL", 1, OrderSide.SELL, OrderType.LIMIT, 200.0);
        orderService.placeOrder(order);
        Stock stock = new Stock("AAPL", 210.0); // above limit

        // Act
        orderService.onPriceUpdate(stock);

        // Assert
        assertEquals(OrderStatus.EXECUTED, order.getStatus());
    }

    @Test
    void notificationFiredOnOrderExecution() {
        // Arrange
        Stock stock = new Stock("AAPL", 100.0);
        when(marketFeed.getStock("AAPL")).thenReturn(stock);
        Order order = new Order("AAPL", 1, OrderSide.BUY, OrderType.MARKET, 0);

        // Act
        orderService.placeOrder(order);

        // Assert
        verify(notificationService, times(1)).notifyOrderExecuted(order);
    }
}