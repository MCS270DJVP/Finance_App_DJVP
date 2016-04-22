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
    private List<BigDecimal> mHistoricalPrices;
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
            if (mFund.getHistoricalPrices() == null) {
                try {
                    mHistoricalPrices = new ArrayList<BigDecimal>();
                    Stock stocks = YahooFinance.get(mFund.getTicker(), from, to, Interval.DAILY);
                    List<HistoricalQuote> mQuotes = stocks.getHistory();
                    for (HistoricalQuote quote : mQuotes) {
                        if (quote != null) {
                            mHistoricalPrices.add(quote.getAdjClose());
                        }
                    }
                    FundLab.get(mContext).getFund(mFund.getId()).setHistoricalPrices(mHistoricalPrices);
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


