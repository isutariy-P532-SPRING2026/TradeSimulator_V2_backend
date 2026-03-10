package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Stock;

public interface PriceObserver {
    void onPriceUpdate(Stock stock);
}