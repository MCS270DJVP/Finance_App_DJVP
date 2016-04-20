package com.example.dennis.mutualfund;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * Created by huyviet1995 on 4/19/16.
 */
public class YahooFetch extends AsyncTask<String,Void,Fund> {
    private String mTickerTitle;
    private BigDecimal mStockPrice;
    private Context mContext;

    private static final String TAG = "TAG";
    public YahooFetch(Context context, String tickerTitle) {
        mTickerTitle = tickerTitle;
        mContext = context;
    }
    private Fund fund;
    @Override
    protected Fund doInBackground(String... params) {

        try {
            Log.i(TAG,"Successfully execute");
            Stock stock= YahooFinance.get(mTickerTitle);
            mStockPrice = stock.getQuote().getPrice();
            fund = new Fund();
            fund.setTicker(mTickerTitle);
            fund.setStockValue(mStockPrice);
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
