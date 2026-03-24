package com.tradesim.tradesimulator.service;

import java.util.List;  
import java.util.ArrayList;
import com.tradesim.tradesimulator.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotificationDecoratorChainTest {

    @Test
    void decoratorChainFiresAllWrappedNotifiers() {
        // Arrange
        java.util.List<String> log = new java.util.ArrayList<>();
        NotificationService base = order -> log.add("CONSOLE");
        NotificationService withEmail = new EmailNotificationDecorator(base) {
            @Override public void notifyOrderExecuted(Order o) {
                super.notifyOrderExecuted(o); log.add("EMAIL");
            }
        };
        NotificationService withSms = new SmsNotificationDecorator(withEmail) {
            @Override public void notifyOrderExecuted(Order o) {
                super.notifyOrderExecuted(o); log.add("SMS");
            }
        };

        Order order = new Order("AAPL", 1, OrderSide.BUY, OrderType.MARKET, 0);
        order.markExecuted(100.0);

        // Act
        withSms.notifyOrderExecuted(order);

        // Assert — all three fired
        assertTrue(log.contains("CONSOLE"));
        assertTrue(log.contains("EMAIL"));
        assertTrue(log.contains("SMS"));
    }

    @Test
    void mutableNotificationServiceSwapsDelegate() {
        // Arrange
        java.util.List<String> log = new java.util.ArrayList<>();
        MutableNotificationService mns = new MutableNotificationService(o -> log.add("A"));
        Order order = new Order("AAPL", 1, OrderSide.BUY, OrderType.MARKET, 0);
        order.markExecuted(100.0);

        // Act — fire with first delegate
        mns.notifyOrderExecuted(order);
        // Swap delegate
        mns.setDelegate(o -> log.add("B"));
        mns.notifyOrderExecuted(order);

        // Assert
        assertEquals(List.of("A", "B"), log);
    }
}