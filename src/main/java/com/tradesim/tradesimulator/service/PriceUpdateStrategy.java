package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.Stock;
import java.util.Random;

public interface PriceUpdateStrategy {
    double calculateNewPrice(Stock stock, Random random);
}