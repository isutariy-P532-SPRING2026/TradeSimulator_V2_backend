package com.tradesim.tradesimulator.controller;

import com.tradesim.tradesimulator.factory.OrderFactory;
import com.tradesim.tradesimulator.model.*;
import com.tradesim.tradesimulator.service.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TradingController {

    private final MarketFeed marketFeed;
    private final UserRegistry userRegistry;
    private final OrderFactory orderFactory;
    private final DashboardAlertStore alertStore;

    public TradingController(MarketFeed marketFeed, UserRegistry userRegistry,
                             OrderFactory orderFactory, DashboardAlertStore alertStore) {
        this.marketFeed = marketFeed;
        this.userRegistry = userRegistry;
        this.orderFactory = orderFactory;
        this.alertStore = alertStore;
    }

    @GetMapping("/users")
    public List<Map<String, String>> getUsers() {
        return userRegistry.getAllUsers().values().stream()
            .map(u -> Map.of("id", u.getId(), "name", u.getName()))
            .toList();
    }

    @GetMapping("/stocks")
    public Map<String, Stock> getStocks() {
        return marketFeed.getStocks();
    }

    @GetMapping("/portfolio")
    public Map<String, Object> getPortfolio(@RequestParam(defaultValue = "alice") String userId) {
        Portfolio portfolio = userRegistry.getPortfolio(userId);
        double totalValue = portfolio.getCashBalance();
        for (Map.Entry<String, Integer> entry : portfolio.getHoldings().entrySet()) {
            Stock stock = marketFeed.getStock(entry.getKey());
            if (stock != null) totalValue += stock.getCurrentPrice() * entry.getValue();
        }
        return Map.of(
            "cashBalance", portfolio.getCashBalance(),
            "holdings", portfolio.getHoldings(),
            "totalValue", totalValue
        );
    }

    @GetMapping("/orders")
    public Map<String, Object> getOrders(@RequestParam(defaultValue = "alice") String userId) {
        OrderService os = userRegistry.getOrderService(userId);
        return Map.of("pending", os.getPendingOrders(), "history", os.getTradeHistory());
    }

    @PostMapping("/orders")
    public Map<String, String> placeOrder(@RequestParam(defaultValue = "alice") String userId,
                                          @RequestBody Map<String, String> request) {
        try {
            Order order = orderFactory.createOrder(
                request.get("ticker"),
                Integer.parseInt(request.get("quantity")),
                request.get("side"),
                request.get("orderType"),
                request.containsKey("limitPrice") ?
                    Double.parseDouble(request.get("limitPrice")) : 0.0
            );
            String result = userRegistry.getOrderService(userId).placeOrder(order);
            return Map.of("message", result);
        } catch (IllegalArgumentException e) {
            return Map.of("error", e.getMessage());
        }
    }

    // Change 2 — configure notification channels per user
    @PostMapping("/notifications")
    public Map<String, String> updateNotifications(
            @RequestParam(defaultValue = "alice") String userId,
            @RequestBody Map<String, List<String>> request) {
        Set<NotificationChannels> channels = request.get("channels").stream()
            .map(c -> NotificationChannels.valueOf(c.toUpperCase()))
            .collect(Collectors.toSet());
        channels.add(NotificationChannels.CONSOLE); // always on
        userRegistry.updateNotificationChannels(userId, channels);
        return Map.of("message", "Notification channels updated");
    }

    // Change 2 — dashboard badge count
    @GetMapping("/alerts")
    public Map<String, Object> getAlerts(@RequestParam(defaultValue = "alice") String userId) {
        int count = alertStore.getCount(userId);
        alertStore.clear(userId);   // clear on read
        return Map.of("unread", count);
    }

    // Change 2 — current channel config for a user
    @GetMapping("/notifications")
    public Map<String, Object> getNotifications(@RequestParam(defaultValue = "alice") String userId) {
        User user = userRegistry.getUser(userId);
        return Map.of("channels", user.getChannels().stream()
            .map(Enum::name).toList());
    }
}