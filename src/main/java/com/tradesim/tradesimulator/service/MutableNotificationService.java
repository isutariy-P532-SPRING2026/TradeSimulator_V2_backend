package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Order;

public class MutableNotificationService implements NotificationService {

    private volatile NotificationService delegate;

    public MutableNotificationService(NotificationService delegate) {
        this.delegate = delegate;
    }

    public void setDelegate(NotificationService delegate) {
        this.delegate = delegate;
    }

    @Override
    public void notifyOrderExecuted(Order order) {
        delegate.notifyOrderExecuted(order);
    }
}