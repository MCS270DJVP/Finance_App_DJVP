package com.example.dennis.mutualfund;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class FundFragment extends Fragment{
    private Fund mFund;
    private RecyclerView mFundRecyclerView;
    private EditText mTickerField;
    private TextView mPriceField;
    private Button mAddButton;
    private Button mRemoveButton;
    private Button mCalculate;
    private static final String TAG = "MUTUAL_FUND";
    private FundAdapter mFundAdapter;
    private BigDecimal mStockPrice;
    private String mTickerTitle;
    private List<Fund> mFunds;


    @Override
    public void onCreate(Bundle savedInstancestate){
        super.onCreate(savedInstancestate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_fund, container, false);
        mFundRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mFundRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTickerField = (EditText) v.findViewById(R.id.ticker_text);
        mAddButton = (Button) v.findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mTickerTitle = mTickerField.getText().toString();
                if (!isEmpty(mTickerField) && isValidedString(mTickerTitle)) {
                    new FetchYahooTask().execute();
                    mTickerField.setText("");
                }
            }
        });
        mCalculate = (Button) v.findViewById(R.id.calculate_button);
        updateUI();
        return v;
    }
    private class FundHolder extends RecyclerView.ViewHolder {
        private Fund mFund;
        private TextView mTickerTextView;
        private Spinner mWeightScrollerView;
        public FundHolder(View itemView){
            super(itemView);
            mTickerTextView = (TextView) itemView.findViewById(R.id.list_item_ticker_textview);
            mPriceField = (TextView) itemView.findViewById(R.id.price_display);
            mRemoveButton = (Button) itemView.findViewById(R.id.remove);
            mRemoveButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    mFunds.remove(mFund);
                    updateUI();
                }
            });
        }
        public void bindFund(Fund fund){
            mFund = fund;
            mTickerTextView.setText(mFund.getTicker().toUpperCase());

            if (mFund.getStockValue()!=null ) {
                mPriceField.setText(mFund.getStockValue().toString());
            }
            else mPriceField.setText("NA");
        }
    }
    private class FundAdapter extends RecyclerView.Adapter<FundHolder> {
        private List<Fund> mFunds;
        public FundAdapter(List<Fund> funds) {
            mFunds = funds;
        }
        @Override
        public FundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_fund, parent, false);
            return new FundHolder(view);
        }
        @Override
        public void onBindViewHolder(FundHolder holder, int position){
            Fund fund = mFunds.get(position);
            holder.bindFund(fund);
        }
        @Override
        public int getItemCount() {
            return mFunds.size();
        }
        /*if mFundAdapter is null then create a new mFundAdapter*/
        public void setFunds(List<Fund>funds) {mFunds = funds;}
    }
    private class FetchYahooTask extends AsyncTask<Void,Void,List<Fund>> {
        Stock stock;
        @Override
        protected List<Fund> doInBackground(Void... params) {
            try {
                Log.i(TAG,"Successfully execute");
                stock= YahooFinance.get(mTickerTitle);
                mStockPrice = stock.getQuote().getPrice();
                /*Create a new fund with data fetched from the internet*/
                Fund fund = new Fund();
                fund.setStockValue(mStockPrice);
                fund.setTicker(mTickerTitle);
                mFunds.add(fund);
            } catch (IOException e) {
                Log.i(TAG,"Fail to execute",e);
            }
            return mFunds;
        }
        @Override
        protected void onPostExecute(List<Fund> funds) {
            updateUI();
        }
    }
    /*update user interface*/
    private void updateUI() {
        /*set the fund */
        mFunds = FundLab.get(getActivity()).getFunds();

        if (mFundAdapter == null) {
            mFundAdapter = new FundAdapter(mFunds);
            mFundRecyclerView.setAdapter(mFundAdapter);
        }
        else {
            mFundAdapter.setFunds(mFunds);
            mFundAdapter.notifyDataSetChanged();
        }
        mFundRecyclerView.setAdapter(mFundAdapter);
    }
    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo !=null &&  activeNetworkInfo.isConnected();
    }
    private boolean isValidedString(String str) {
        for (int i = 0; i < str.length();i++) {
            if (str.charAt(i) == ' ') {
                return false;
            }
        }
        return true;
    }
    private boolean isEmpty (EditText text) {
        if (text.getText().toString().length() > 0) {
            return false;
        }
        else return true;
    }

}


