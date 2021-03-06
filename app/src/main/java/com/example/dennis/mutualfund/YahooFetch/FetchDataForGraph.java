package com.example.dennis.mutualfund.YahooFetch;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.example.dennis.mutualfund.Fund;
import com.example.dennis.mutualfund.FundLab;
import com.example.dennis.mutualfund.GraphDialogFragment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class FetchDataForGraph extends AsyncTask<Fund,Void,Fund>{
    private Fund mFund;
    private Context mContext;
    private List<Double> mHistoricalPrices;
    private Runnable mContinuation;
    public FetchDataForGraph (Context context,Fund fund, Runnable continuation) {
        mFund = fund;
        mContext = context;
        mContinuation = continuation;
    }
    @Override
    protected Fund doInBackground(Fund... params) {
        Calendar currentTime = Calendar.getInstance();
        Calendar pastTime = mFund.getTime();
        /*check if the current time is after the time when historical prices were fetched by one day
        * or after the trading market is closed at 4pm of the same day
        *If that is the case, update new data*/
        boolean isSameDate = currentTime.get(Calendar.DAY_OF_YEAR) == pastTime.get(Calendar.DAY_OF_YEAR)
                && currentTime.get(Calendar.YEAR) == pastTime.get(Calendar.YEAR)
                && (currentTime.get(Calendar.HOUR_OF_DAY) >=16 && pastTime.get(Calendar.HOUR_OF_DAY ) >= 16
                || (currentTime.get(Calendar.HOUR_OF_DAY) <16 && pastTime.get(Calendar.HOUR_OF_DAY ) < 16));
        if (mFund.getHistoricalPrices() !=null && isSameDate) {
            return mFund;
        }
        else {
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();
            from.add(Calendar.YEAR, -1);
            try {
                mHistoricalPrices = new ArrayList<Double>();
                Stock stocks = YahooFinance.get(mFund.getTicker(),from,to, Interval.DAILY);
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
        return mFund;
    }
    @Override
    protected void onPostExecute(Fund fund) {
        mContinuation.run();
    }
}
