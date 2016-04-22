package com.example.dennis.mutualfund.YahooFetch;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import com.example.dennis.mutualfund.Fund;
import com.example.dennis.mutualfund.FundLab;
import com.example.dennis.mutualfund.GraphDialogFragment;
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
public class FetchDataForGraph extends AsyncTask<Fund,Void,DialogFragment>{
    private Fund mFund;
    private Context mContext;
    private List<BigDecimal> mHistoricalPrices;
    private FragmentManager mManager;
    public FetchDataForGraph (Context context,FragmentManager manager, Fund fund) {
        mManager = manager;
        mFund = fund;
        mContext = context;
    }
    @Override
    protected DialogFragment doInBackground(Fund... params) {
        if (mFund.getHistoricalPrices() !=null) {
            return GraphDialogFragment.newInstance(mFund);
        }
        else {
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();
            from.add(Calendar.YEAR, -1);
            try {
                mHistoricalPrices = new ArrayList<BigDecimal>();
                Stock stocks = YahooFinance.get(mFund.getTicker(),from,to, Interval.DAILY);
                List<HistoricalQuote> mQuotes = stocks.getHistory();
                for (HistoricalQuote quote : mQuotes) {
                    if (quote != null) {
                        mHistoricalPrices.add(quote.getAdjClose());
                    }
                }
                mFund.setHistoricalPrices(mHistoricalPrices);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return GraphDialogFragment.newInstance(mFund);
    }
    @Override
    protected void onPostExecute(DialogFragment dialogFragment) {
        dialogFragment.show(mManager,"NULL");
    }
}
