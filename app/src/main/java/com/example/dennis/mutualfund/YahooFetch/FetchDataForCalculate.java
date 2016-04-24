package com.example.dennis.mutualfund.YahooFetch;

import android.content.Context;
import android.os.AsyncTask;

import com.example.dennis.mutualfund.Fund;
import com.example.dennis.mutualfund.FundLab;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

/**
 * Created by huyviet1995 on 4/22/16.
 */
public class FetchDataForCalculate extends AsyncTask<List<Fund>, Void,List<Fund> >{
    private Context mContext;
    private List<Double> mHistoricalPrices;
    private Runnable mContinuation;
    public FetchDataForCalculate(Context context, Runnable continuation) {
        mContext = context;
        mContinuation = continuation;
    }
    @Override
    protected List<Fund> doInBackground(List<Fund>... params) {


        List<Fund> mFunds = FundLab.get(mContext).getFunds();
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.YEAR, -1);
        for (int i = 0; i < mFunds.size(); i++) {
            Fund mFund = mFunds.get(i);
             /*update the current price of the time when the calculate button is pressed*/
            try {
                Stock stock = YahooFinance.get(mFund.getTicker().trim());

            } catch (IOException e) {
                e.printStackTrace();
            }

            Calendar currentTime = Calendar.getInstance();
            Calendar pastTime = mFund.getTime();
            /*check if the current time is after the time when historical prices were fetched by one day
            * or after the trading market is closed at 4pm of the same day
            *If that is the case, update new data*/
            boolean isSameDate = currentTime.get(Calendar.DAY_OF_YEAR) == pastTime.get(Calendar.DAY_OF_YEAR)
                    && currentTime.get(Calendar.YEAR) == pastTime.get(Calendar.YEAR)
                    && currentTime.get(Calendar.HOUR_OF_DAY) <=16;
            if (mFund.getHistoricalPrices() == null && !isSameDate) {
                try {
                    mHistoricalPrices = new ArrayList<Double>();
                    Stock stocks = YahooFinance.get(mFund.getTicker(), from, to, Interval.DAILY);
                    List<HistoricalQuote> mQuotes = stocks.getHistory();
                    for (HistoricalQuote quote : mQuotes) {
                        if (quote != null) {
                            mHistoricalPrices.add(quote.getAdjClose().doubleValue());
                        }
                    }
                    mFund.setHistoricalPrices(mHistoricalPrices);
                    mFund.setTime(Calendar.getInstance());
                    FundLab.get(mContext).updateFund(mFund);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }
        return mFunds;
    }
    @Override
    protected void onPostExecute(List<Fund> funds) {
        mContinuation.run();
    }
}


