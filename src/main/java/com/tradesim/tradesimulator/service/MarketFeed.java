package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Stock;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service  // Singleton — Spring creates exactly one instance
@EnableScheduling
public class MarketFeed {

    // Strategy pattern — depends on interface, not implementation
    private final PriceUpdateStrategy pricingStrategy;
    private final Random random;

    // Observer pattern — list of listeners
    private final List<PriceObserver> observers = new ArrayList<>();

    private final Map<String, Stock> stocks = new LinkedHashMap<>();

    public MarketFeed(PriceUpdateStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
        this.random = new Random();

        // Hardcoded stocks with initial prices
        List.of(
            new Stock("AAPL", 180.0),
            new Stock("GOOG", 140.0),
            new Stock("TSLA", 250.0),
            new Stock("AMZN", 185.0),
            new Stock("MSFT", 420.0)
        ).forEach(s -> stocks.put(s.getTicker(), s));
    }

    // Runs every 5 seconds automatically
    @Scheduled(fixedRate = 5000)
    public void updatePrices() {
        for (Stock stock : stocks.values()) {
            double newPrice = pricingStrategy.calculateNewPrice(stock, random);
            stock.setCurrentPrice(newPrice);
            notifyObservers(stock);  // tell all listeners
        }
    }

    // Observer pattern — register a listener
    public void addObserver(PriceObserver observer) {
        observers.add(observer);
    }

    // Observer pattern — broadcast to all listeners
    private void notifyObservers(Stock stock) {
        for (PriceObserver observer : observers) {
            observer.onPriceUpdate(stock);
        }
    }

    public Map<String, Stock> getStocks() { return stocks; }
    public Stock getStock(String ticker) { return stocks.get(ticker); }
}