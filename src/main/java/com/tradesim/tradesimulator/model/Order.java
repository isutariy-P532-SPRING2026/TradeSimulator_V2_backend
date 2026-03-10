package com.tradesim.tradesimulator.model;

import java.time.LocalDateTime;

public class Order {
    private String ticker;
    private int quantity;
    private OrderSide side;
    private OrderType orderType;
    private double limitPrice;
    private OrderStatus status;
    private double executedPrice;
    private LocalDateTime timestamp;

    public Order(String ticker, int quantity, OrderSide side, OrderType orderType, double limitPrice) {
        this.ticker = ticker;
        this.quantity = quantity;
        this.side = side;
        this.orderType = orderType;
        this.limitPrice = limitPrice;
        this.status = OrderStatus.PENDING;
        this.timestamp = LocalDateTime.now();
    }

    public String getTicker() { return ticker; }
    public int getQuantity() { return quantity; }
    public OrderSide getSide() { return side; }
    public OrderType getOrderType() { return orderType; }
    public double getLimitPrice() { return limitPrice; }
    public OrderStatus getStatus() { return status; }
    public double getExecutedPrice() { return executedPrice; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void markExecuted(double price) {
        this.executedPrice = price;
        this.status = OrderStatus.EXECUTED;
        this.timestamp = LocalDateTime.now();
    }
}