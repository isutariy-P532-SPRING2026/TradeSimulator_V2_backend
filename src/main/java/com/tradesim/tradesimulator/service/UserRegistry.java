package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Portfolio;
import com.tradesim.tradesimulator.model.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserRegistry {

    private final Map<String, User> users = new LinkedHashMap<>();
    private final Map<String, Portfolio> portfolios = new LinkedHashMap<>();
    private final Map<String, OrderService> orderServices = new LinkedHashMap<>();
    private final Map<String, MutableNotificationService> notifServices = new LinkedHashMap<>();

    private final DashboardAlertStore alertStore;

    public UserRegistry(MarketFeed marketFeed, DashboardAlertStore alertStore) {
        this.alertStore = alertStore;

        for (String[] ud : new String[][]{
                {"alice", "Alice"},
                {"bob", "Bob"},
                {"carol", "Carol"}
        }) {
            register(ud[0], ud[1], marketFeed);
        }
    }

    private void register(String id, String name, MarketFeed marketFeed) {
        User user = new User(id, name);
        users.put(id, user);

        Portfolio portfolio = new Portfolio();
        portfolios.put(id, portfolio);

        MutableNotificationService mns = new MutableNotificationService(
                NotificationBuilder.build(user, alertStore)
        );
        notifServices.put(id, mns);

        // OrderService is per-user (not a Spring bean)
        orderServices.put(id, new OrderService(portfolio, marketFeed, mns));
    }

    public User getUser(String id) {
        return users.get(id);
    }

    public Portfolio getPortfolio(String id) {
        return portfolios.get(id);
    }

    public OrderService getOrderService(String id) {
        return orderServices.get(id);
    }

    public Map<String, User> getAllUsers() {
        return users;
    }

    public void updateNotificationChannels(String userId,
                                           Set<NotificationChannels> channels) {
        User user = users.get(userId);
        if (user == null) return;

        user.setChannels(channels);

        // Rebuild notification chain using injected alertStore
        notifServices.get(userId)
                .setDelegate(NotificationBuilder.build(user, alertStore));
    }
}