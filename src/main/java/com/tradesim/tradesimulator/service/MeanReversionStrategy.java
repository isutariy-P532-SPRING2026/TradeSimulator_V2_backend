package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Stock;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@Primary
public class MeanReversionStrategy implements PriceUpdateStrategy {

    private static final int WINDOW = 10;
    private final Map<String, Queue<Double>> history = new HashMap<>();

    @Override
    public double calculateNewPrice(Stock stock, Random random) {
        history.putIfAbsent(stock.getTicker(), new LinkedList<>());
        Queue<Double> window = history.get(stock.getTicker());
        window.add(stock.getCurrentPrice());
        if (window.size() > WINDOW) window.poll();

        double mean = window.stream().mapToDouble(d -> d).average()
                           .orElse(stock.getCurrentPrice());
        double pull = (stock.getCurrentPrice() - mean) * 0.1;
        double noise = (random.nextDouble() * 2 - 1) * stock.getCurrentPrice() * 0.005;
        return Math.max(1.0, stock.getCurrentPrice() - pull + noise);
    }
}