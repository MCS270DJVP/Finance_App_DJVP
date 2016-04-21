package com.example.dennis.mutualfund;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

/**
 * Created by huyviet1995 on 4/19/16.
 */
public class YahooFetch extends AsyncTask<String,Void,Fund> {
    private String mTickerTitle;
    private List<HistoricalQuote> mQuotes;
    private BigDecimal mStockPrice;
    private Context mContext;
    private Runnable mContinuation;

    private static final String TAG = "TAG";
    public YahooFetch(Context context, String tickerTitle, Runnable continuation) {
        mTickerTitle = tickerTitle;
        mContext = context;
        mContinuation = continuation;
    }
    private Fund fund;
    @Override
    protected Fund doInBackground(String... params) {

        try {
            Log.i(TAG,"Successfully execute");


            /*pulling down historical data using Yahoo API*/
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();
            from.add(Calendar.YEAR, -1);
            Stock stocks =YahooFinance.get(mTickerTitle,from,to, Interval.DAILY);
            mQuotes = stocks.getHistory();
            Stock stock = YahooFinance.get(mTickerTitle);
            mStockPrice = stock.getQuote().getPrice();
            fund = new Fund();
            fund.setTicker(mTickerTitle);
            fund.setHistoricalQuotes(mQuotes);
                /*Create a new fund with data fetched from the internet*/
        } catch (IOException e) {
            Log.i(TAG,"Fail to execute",e);
        }
        return fund;
    }
    @Override
    protected void onPostExecute(Fund fund) {
            /*after the background thread is executed, updating the mFunds*/
        if (mStockPrice != null) {
            FundLab.get(mContext).addFund(fund);
            mContinuation.run();
        }
        else {
            dialog("Invalid ticker");
        }
    }
    private void dialog(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(message);
        dialog.setPositiveButton(R.string.positive_button,null);
        dialog.show();
    }

}
