package com.tradesim.tradesimulator.controller;

import com.tradesim.tradesimulator.factory.OrderFactory;
import com.tradesim.tradesimulator.model.*;
import com.tradesim.tradesimulator.service.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TradingController {

    private final MarketFeed marketFeed;
    private final OrderService orderService;
    private final Portfolio portfolio;
    private final OrderFactory orderFactory;

    public TradingController(MarketFeed marketFeed, OrderService orderService,
                             Portfolio portfolio, OrderFactory orderFactory) {
        this.marketFeed = marketFeed;
        this.orderService = orderService;
        this.portfolio = portfolio;
        this.orderFactory = orderFactory;
    }

    // GET /api/stocks — returns current stock prices
    @GetMapping("/stocks")
    public Map<String, Stock> getStocks() {
        return marketFeed.getStocks();
    }

    // GET /api/portfolio — returns cash, holdings, total value
    @GetMapping("/portfolio")
    public Map<String, Object> getPortfolio() {
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

    // GET /api/orders — returns pending and executed orders
    @GetMapping("/orders")
    public Map<String, Object> getOrders() {
        return Map.of(
            "pending", orderService.getPendingOrders(),
            "history", orderService.getTradeHistory()
        );
    }

    // POST /api/orders — place a new order
    @PostMapping("/orders")
    public Map<String, String> placeOrder(@RequestBody Map<String, String> request) {
        try {
            Order order = orderFactory.createOrder(
                request.get("ticker"),
                Integer.parseInt(request.get("quantity")),
                request.get("side"),
                request.get("orderType"),
                request.containsKey("limitPrice") ? 
                    Double.parseDouble(request.get("limitPrice")) : 0.0
            );
            String result = orderService.placeOrder(order);
            return Map.of("message", result);
        } catch (IllegalArgumentException e) {
            return Map.of("error", e.getMessage());
        }
    }
}