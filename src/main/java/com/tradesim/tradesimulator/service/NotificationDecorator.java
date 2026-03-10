package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Order;

public abstract class NotificationDecorator implements NotificationService {
    
    protected final NotificationService wrapped;

    public NotificationDecorator(NotificationService wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void notifyOrderExecuted(Order order) {
        wrapped.notifyOrderExecuted(order);  // call the inner one first
    }
}