package com.example.dennis.mutualfund.YahooFetch;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.dennis.mutualfund.Fund;
import com.example.dennis.mutualfund.FundLab;
import com.example.dennis.mutualfund.GraphDialogFragment;
import com.example.dennis.mutualfund.R;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class FetchDataForAdd extends AsyncTask<String,Void,Fund> {
    private String mTickerTitle;
    private Context mContext;
    private ArrayList<Double> mHistoricalPrices;
    private Runnable mContinuation;

    private static final String TAG = "TAG";
    public FetchDataForAdd(Context context, String tickerTitle, Runnable continuation) {
        mTickerTitle = tickerTitle;
        mContext = context;
        mContinuation = continuation;
    }
    private Fund fund;


    /* This function now fetches historical data for the fund in question, rather
     *  than simply verifying that the fund exists. - Jack P */
    @Override
    protected Fund doInBackground(String... params) {
        Fund mFund = new Fund();
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.YEAR, -1);
        try {
            mHistoricalPrices = new ArrayList<Double>();
            Stock stocks = YahooFinance.get(mTickerTitle,from,to, Interval.DAILY);
            List<HistoricalQuote> mQuotes = stocks.getHistory();
            for (HistoricalQuote quote : mQuotes) {
                if (quote != null) {
                        mHistoricalPrices.add(quote.getAdjClose().doubleValue());
                }
            }
            mFund.setHistoricalPrices(mHistoricalPrices);
            mFund.setTicker(mTickerTitle);
            mFund.setTime(Calendar.getInstance());
            FundLab.get(mContext).updateFund(mFund);
        } catch (IOException e) {
                e.printStackTrace();
            }
        return mFund;
        }

    @Override
    protected void onPostExecute(Fund fund) {
        if (mHistoricalPrices != null) {
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
