package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Order;

public class SmsNotificationDecorator extends NotificationDecorator {

    public SmsNotificationDecorator(NotificationService wrapped) {
        super(wrapped);
    }

    @Override
    public void notifyOrderExecuted(Order order) {
        super.notifyOrderExecuted(order);
        System.out.printf("SMS: %s %s x%d executed%n",
            order.getSide(), order.getTicker(), order.getQuantity());
    }
}