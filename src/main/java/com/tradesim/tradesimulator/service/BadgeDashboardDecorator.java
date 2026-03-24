package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Order;

public class BadgeDashboardDecorator extends NotificationDecorator {

    private final DashboardAlertStore alertStore;
    private final String userId;

    public BadgeDashboardDecorator(NotificationService wrapped,
                                   DashboardAlertStore alertStore,
                                   String userId) {
        super(wrapped);
        this.alertStore = alertStore;
        this.userId = userId;
    }

    @Override
    public void notifyOrderExecuted(Order order) {
        super.notifyOrderExecuted(order);
        alertStore.increment(userId);
        System.out.println("DASHBOARD [" + userId + "]: "
            + alertStore.getCount(userId) + " unread alert(s)");
    }
}