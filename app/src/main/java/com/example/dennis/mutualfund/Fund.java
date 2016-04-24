package com.example.dennis.mutualfund;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import yahoofinance.histquotes.HistoricalQuote;

public class Fund implements Serializable {

    private UUID mId;
    private String mTicker;
    private Double mStockPrice;
    private List<Double> mHistoricalPrices;
    private int mWeight;
    private Calendar mCalendar;

    public Fund() {
        mId = UUID.randomUUID();
    }

    public Fund(UUID id) {mId = id;}

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

    public Double getStockValue() {
        return mStockPrice;
    }

    public void setStockValue(Double stockPrice) {
        mStockPrice = stockPrice;
    }

    public void setHistoricalPrices(List<Double> prices) {
        mHistoricalPrices = prices;
    }

    public List<Double> getHistoricalPrices() {
        return mHistoricalPrices;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int newWeight) {
        mWeight= newWeight;
    }

    public void setTime(Calendar time) {
        mCalendar = time;
    }

    public Calendar getTime() {
        return mCalendar;
    }

}