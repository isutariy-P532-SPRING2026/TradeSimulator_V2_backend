package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Order;

public class EmailNotificationDecorator extends NotificationDecorator {

    public EmailNotificationDecorator(NotificationService wrapped) {
        super(wrapped);
    }

    @Override
    public void notifyOrderExecuted(Order order) {
        super.notifyOrderExecuted(order);
        System.out.printf("EMAIL: Order executed — %s %d shares of %s @ $%.2f%n",
            order.getSide(), order.getQuantity(),
            order.getTicker(), order.getExecutedPrice());
    }
}