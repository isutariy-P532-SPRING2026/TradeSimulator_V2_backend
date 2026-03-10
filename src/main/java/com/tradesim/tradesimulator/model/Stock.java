package com.tradesim.tradesimulator.model;

public class Stock {
    private String ticker;
    private double currentPrice;
    private double previousPrice;

    public Stock(String ticker, double initialPrice) {
        this.ticker = ticker;
        this.currentPrice = initialPrice;
        this.previousPrice = initialPrice;
    }

    public String getTicker() { return ticker; }
    public double getCurrentPrice() { return currentPrice; }
    public double getPreviousPrice() { return previousPrice; }

    public void setCurrentPrice(double newPrice) {
        this.previousPrice = this.currentPrice;
        this.currentPrice = newPrice;
    }
}