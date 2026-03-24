package com.tradesim.tradesimulator.service;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
public class DashboardAlertStore {

    private final Map<String, Integer> unread = new ConcurrentHashMap<>();

    public void increment(String userId) {
        unread.merge(userId, 1, Integer::sum);
    }

    public int getCount(String userId) {
        return unread.getOrDefault(userId, 0);
    }

    public void clear(String userId) {
        unread.put(userId, 0);
    }
}