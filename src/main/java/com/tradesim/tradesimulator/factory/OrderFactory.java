package com.tradesim.tradesimulator.factory;

import com.tradesim.tradesimulator.model.*;
import org.springframework.stereotype.Component;

@Component
public class OrderFactory {

    public Order createOrder(String ticker, int quantity, 
                             String side, String orderType, 
                             double limitPrice) {
        OrderSide orderSide;
        try {
            orderSide = OrderSide.valueOf(side.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid side: " + side + ". Use BUY or SELL");
        }

        OrderType type;
        try {
            type = OrderType.valueOf(orderType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order type: " + orderType + ". Use MARKET or LIMIT");
        }

        return new Order(ticker, quantity, orderSide, type, limitPrice);
    }
}