package com.example.dennis.mutualfund.YahooFetch;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.dennis.mutualfund.Fund;
import com.example.dennis.mutualfund.FundLab;
import com.example.dennis.mutualfund.R;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistQuotesRequest;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

/**
 * Created by huyviet1995 on 4/19/16.
 */
public class FetchDataForAdd extends AsyncTask<String,Void,Fund> {
    private String mTickerTitle;
    private List<HistoricalQuote> mQuotes;
    private List<BigDecimal> mHistoricalPrices;
    private Context mContext;
    private Double mStockPrice;
    private Runnable mContinuation;

    private static final String TAG = "TAG";
    public FetchDataForAdd(Context context, String tickerTitle, Runnable continuation) {
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
            /*loop through the quotes to get the close price of the day*/
            fund = new Fund();
            fund.setTicker(mTickerTitle);
            Stock stock = YahooFinance.get(mTickerTitle);
            mStockPrice = stock.getQuote().getPrice().doubleValue();
            fund.setStockValue(mStockPrice);
            /*set the time when the fund is added*/
            fund.setTime(Calendar.getInstance());
        } catch (IOException e) {
            Log.i(TAG,"Fail to execute",e);
        }
        return fund;
    }
    @Override
    protected void onPostExecute(Fund fund) {
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