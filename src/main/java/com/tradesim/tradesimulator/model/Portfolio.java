package com.tradesim.tradesimulator.model;

import java.util.HashMap;
import java.util.Map;

public class Portfolio {
    private double cashBalance;
    private Map<String, Integer> holdings;

    public Portfolio() {
        this.cashBalance = 10000.0;
        this.holdings = new HashMap<>();
    }

    public double getCashBalance() { return cashBalance; }
    public Map<String, Integer> getHoldings() { return holdings; }

    public void deductCash(double amount) { this.cashBalance -= amount; }
    public void addCash(double amount) { this.cashBalance += amount; }

    public void addShares(String ticker, int qty) {
        holdings.merge(ticker, qty, Integer::sum);
    }

    public void removeShares(String ticker, int qty) {
        holdings.merge(ticker, -qty, Integer::sum);
    }

    public int getShares(String ticker) {
        return holdings.getOrDefault(ticker, 0);
    }
}