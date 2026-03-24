package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


public class OrderService implements PriceObserver {

    private final Portfolio portfolio;
    private final MarketFeed marketFeed;
    private final NotificationService notificationService;

    private final List<Order> pendingOrders = new ArrayList<>();
    private final List<Order> tradeHistory = new ArrayList<>();

    public OrderService(Portfolio portfolio, MarketFeed marketFeed,
                        NotificationService notificationService) {
        this.portfolio = portfolio;
        this.marketFeed = marketFeed;
        this.notificationService = notificationService;
        marketFeed.addObserver(this);  // register as observer
    }

    public String placeOrder(Order order) {
        if (order.getOrderType() == OrderType.MARKET) {
            Stock stock = marketFeed.getStock(order.getTicker());
            return executeOrder(order, stock.getCurrentPrice());
        } else {
            pendingOrders.add(order);
            return "Limit order placed";
        }
    }

    // Called automatically by MarketFeed every 5 seconds
    @Override
    public void onPriceUpdate(Stock stock) {
        List<Order> toExecute = new ArrayList<>();

        for (Order order : pendingOrders) {
            if (!order.getTicker().equals(stock.getTicker())) continue;

            boolean shouldExecute =
                (order.getSide() == OrderSide.BUY  
                    && stock.getCurrentPrice() <= order.getLimitPrice()) ||
                (order.getSide() == OrderSide.SELL 
                    && stock.getCurrentPrice() >= order.getLimitPrice());

            if (shouldExecute) toExecute.add(order);
        }

        for (Order order : toExecute) {
            pendingOrders.remove(order);
            executeOrder(order, stock.getCurrentPrice());
        }
    }

    private String executeOrder(Order order, double price) {
        int qty = order.getQuantity();
        double total = price * qty;

        if (order.getSide() == OrderSide.BUY) {
            if (portfolio.getCashBalance() < total)
                return "Insufficient funds";
            portfolio.deductCash(total);
            portfolio.addShares(order.getTicker(), qty);

        } else {
            if (portfolio.getShares(order.getTicker()) < qty)
                return "Insufficient shares";
            portfolio.addCash(total);
            portfolio.removeShares(order.getTicker(), qty);
        }

        order.markExecuted(price);
        tradeHistory.add(order);
        notificationService.notifyOrderExecuted(order);
        return "Order executed at $" + price;
    }

    public List<Order> getPendingOrders() { return pendingOrders; }
    public List<Order> getTradeHistory() { return tradeHistory; }
}