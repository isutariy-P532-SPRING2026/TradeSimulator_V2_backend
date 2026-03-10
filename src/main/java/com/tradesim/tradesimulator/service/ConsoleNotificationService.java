package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Order;
import org.springframework.stereotype.Service;

@Service
public class ConsoleNotificationService implements NotificationService {
    @Override
    public void notifyOrderExecuted(Order order) {
        System.out.println("ORDER EXECUTED: " + order.getSide() 
            + " " + order.getQuantity() 
            + " shares of " + order.getTicker() 
            + " at $" + order.getExecutedPrice());
    }
}