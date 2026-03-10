package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Order;

public interface NotificationService {
    void notifyOrderExecuted(Order order);
}