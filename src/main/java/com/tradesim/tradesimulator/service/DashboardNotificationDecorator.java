package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Order;
import org.springframework.stereotype.Service;

public class DashboardNotificationDecorator extends NotificationDecorator {

    public DashboardNotificationDecorator(NotificationService wrapped) {
        super(wrapped);
    }

    @Override
    public void notifyOrderExecuted(Order order) {
        super.notifyOrderExecuted(order);  // fires console notifier
        // Dashboard notification — Week 2 can add real implementation
        System.out.println("DASHBOARD ALERT: " + order.getTicker() 
            + " " + order.getSide() + " order executed");
    }
}