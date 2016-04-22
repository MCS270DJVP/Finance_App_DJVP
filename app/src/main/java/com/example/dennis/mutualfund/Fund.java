package com.example.dennis.mutualfund;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import yahoofinance.histquotes.HistoricalQuote;

public class Fund implements Serializable {

    private UUID mId;
    private String mTicker;
    private BigDecimal mStockPrice;
    private List<BigDecimal> mHistoricalPrices;

    public Fund() {
        mId = UUID.randomUUID();
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

    public void setHistoricalPrices(List<BigDecimal> prices) {
        mHistoricalPrices = prices;
    }

    public List<BigDecimal> getmHistoricalPrices() {
        return mHistoricalPrices;
    }
}