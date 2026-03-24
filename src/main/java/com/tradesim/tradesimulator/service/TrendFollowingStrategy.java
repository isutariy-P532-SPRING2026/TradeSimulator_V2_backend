package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Stock;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class TrendFollowingStrategy implements PriceUpdateStrategy {

    @Override
    public double calculateNewPrice(Stock stock, Random random) {
        double momentum = stock.getCurrentPrice() - stock.getPreviousPrice();
        double noise = (random.nextDouble() * 2 - 1) * stock.getCurrentPrice() * 0.005;
        return Math.max(1.0, stock.getCurrentPrice() + momentum * 0.3 + noise);
    }
}