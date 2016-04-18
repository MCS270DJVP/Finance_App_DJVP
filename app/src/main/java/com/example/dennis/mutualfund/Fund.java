package com.example.dennis.mutualfund;

import java.math.BigDecimal;
import java.util.UUID;

public class Fund {

    private UUID mId;
    private String mTicker;
    private BigDecimal mStockPrice;
    private int weight;

    public Fund() {
        mId = UUID.randomUUID();
    }
    public Fund(UUID id) {mId = id;}

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID newId) {
        mId = newId;
    }

    public String getTicker() {
        return mTicker;
    }

    public void setTicker(String ticker) {
        mTicker = ticker;
    }

    public BigDecimal getStockValue() {
        return mStockPrice;
    }

    public void setStockValue(BigDecimal stockPrice) {
        mStockPrice = stockPrice;
    }
}