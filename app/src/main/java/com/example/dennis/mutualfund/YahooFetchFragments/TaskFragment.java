package com.example.dennis.mutualfund.YahooFetchFragments;

/**
 * Created by macowner on 5/8/16.
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;

import com.example.dennis.mutualfund.Fund;
import com.example.dennis.mutualfund.FundActivity;
import com.example.dennis.mutualfund.FundLab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

/**
 * This Fragment manages a single background task and retains
 * itself across configuration changes.
 */
public class TaskFragment extends Fragment {

    /**
     * Callback interface through which the fragment will report the
     * task's progress and results back to the Activity.
     */
    public interface TaskCallbacks {
        void onPreExecute();
        void onProgressUpdate(int percent);
        void onCancelled();
        void onPostExecute();
    }

    public TaskCallbacks mCallbacks;
    private DummyTask mTask;
    private Context mContext;



    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mContext = activity;
        mCallbacks = (TaskCallbacks) activity;
    }

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void fetchCalcData () {
        if (mTask == null) {
            mTask = new DummyTask();
            mTask.execute();
        }
    }

    /**
     * A dummy task that performs some (dumb) background work and
     * proxies progress updates and results back to the Activity.
     *
     * Note that we need to check if the callbacks are null in each
     * method in case they are invoked after the Activity's and
     * Fragment's onDestroy() method have been called.
     */
    private class DummyTask extends AsyncTask<List<Fund>, Void, List<Fund>> {

        @Override
        protected void onPreExecute() {
            if (mCallbacks != null) {
                mCallbacks.onPreExecute();
            }
        }

        /**
         * Note that we do NOT call the callback object's methods
         * directly from the background thread, as this could result
         * in a race condition.
         */
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
                        && (currentTime.get(Calendar.HOUR_OF_DAY) >=16 && pastTime.get(Calendar.HOUR_OF_DAY ) >= 16
                        || (currentTime.get(Calendar.HOUR_OF_DAY) <16 && pastTime.get(Calendar.HOUR_OF_DAY ) < 16));
                if (mFund.getHistoricalPrices() == null || !isSameDate) {
                    try {
                        ArrayList<Double> mHistoricalPrices = new ArrayList<Double>();
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
/*
        @Override
        protected void onProgressUpdate(Integer... percent) {
            if (mCallbacks != null) {
                mCallbacks.onProgressUpdate(percent[0]);
            }
        }
*/
        @Override
        protected void onCancelled() {
            if (mCallbacks != null) {
                mTask = null;
                mCallbacks.onCancelled();
            }
        }

        @Override
        protected void onPostExecute(List<Fund> funds) {
            if (mCallbacks != null) {
                mTask = null;
                mCallbacks.onPostExecute();
            }
        }
    }
}