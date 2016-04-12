package com.bignerdranch.android.financeappdjvp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * Created by huyviet1995 on 4/8/16.
 */
public class FinanceMainFragment extends Fragment {
    private Fund mFund;
    private ListOfFunds mFundList;
    private RecyclerView mRecyclerView;
    private ArrayList<Fund> mFunds;
    private FundAdapter mFundAdapter;

    private EditText mSearchView;
    public final String TAG="FinanceMainFragment";
    private Button mAddButton;
    private String mStockTitle;
    private BigDecimal mPrice;
    private TextView mPriceView;

    /*Generate a new instance of FinanceMainFragment*/
    public static FinanceMainFragment newInstance() {
        return new FinanceMainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*make sure that the fragment is preserved when screen is rotated*/
        setRetainInstance(true);

    }

    @Override
    /*Create the main view of the main acitivity, inflate from fragment_main_screen*/
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_screen,container,false);

        /*Set the add button on click listener*/
        Button mAddButton = (Button) view.findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Retrieve the data from editText*/
                mSearchView = (EditText)view.findViewById(R.id.search_button_view);
                /*Get the title input in the search view*/
                mStockTitle = mSearchView.getText().toString();
                new FetchYahooTask().execute();
                updateUI();
            }
        });
        /*get the recycler view from fragment_main_screen*/
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fund_recycler_view);
        /*set the layout for the recycler view*/
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }


    /*Design each list of item on the fund view*/
    public class FundHolder extends RecyclerView.ViewHolder {
        private TextView mTitleView;
        private TextView mPriceView;
        private Fund mFund;
        /*Constructor of the fund holder*/
        public FundHolder(View itemView) {
            super(itemView);
            /*Set the name of the stock and the button right here (Dennis and Peter)*/
            mTitleView = (TextView) itemView.findViewById(R.id.list_item_fund);
            mPriceView = (TextView) itemView.findViewById(R.id.list_stock_price);
        }
        /*Binding the fund*/
        public void bindFund(Fund fund) {
            mFund = fund;
            /*set the name and the title of the text view*/
            mTitleView.setText(mFund.getTitle());
            if (mFund.getPrice()!=null) {
                mPriceView.setText(mFund.getPrice().toString());
            }
            else mPriceView.setText("Price not available");
        }
    }
    /*Wire up the fundHolder to the view using the adapter*/
    private class FundAdapter extends RecyclerView.Adapter<FundHolder> {
        private ArrayList<Fund> mFunds;
        public FundAdapter (ArrayList<Fund> funds) {
            mFunds = funds;
        }
        @Override
        /*Create the view of the list of fund holder*/
        public FundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            /*Inflate view of one list holder*/
            View view = layoutInflater.inflate(R.layout.list_item_fund, parent,false);
            /*return the fundHolder which is attached to the inflated layout*/
            return new FundHolder(view);
        }

        @Override
        /*Bind the fund to the given position*/
        public void onBindViewHolder(FundHolder holder, int position) {
            /*Ordering the funds based on its position in the list of funds
            * This will be used in later binding ranking*/
            Fund fund = mFunds.get(position);
            holder.bindFund(fund);
        }

        @Override
        public int getItemCount() {
            return mFunds.size();
        }
        public void setFunds(ArrayList<Fund> funds) {mFunds = funds;}
    }

    /*Run this task on the background*/
    private class FetchYahooTask extends AsyncTask<Void,Void,ArrayList<Fund>> {
        Stock stock;

        private boolean isConnectedToInternet() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            return activeNetworkInfo !=null &&  activeNetworkInfo.isConnected();
        }

        @Override
        protected ArrayList<Fund> doInBackground(Void... params) {
                try {
                    Log.i(TAG,"Successfully execute");
                    stock= YahooFinance.get(mStockTitle);
                    Fund fund = new Fund(mStockTitle);
                    mPrice = stock.getQuote(true).getPrice();
                    mFunds.add(fund);
                } catch (IOException e) {
                    Log.i(TAG,"Fail to execute",e);
                }


            return mFunds;
        }
        @Override
        protected void onPostExecute(ArrayList<Fund> funds) {
            updateUI();
        }
    }
    /*update user interface*/
    private void updateUI() {
        /*Get all the funds from the list of funds*/
        mFunds = ListOfFunds.get(getActivity()).getFunds();
        /*set the fund adapter to the recyclerView*/
        if (mFundAdapter == null) {
            mFundAdapter = new FundAdapter(mFunds);
            mRecyclerView.setAdapter(mFundAdapter);
        }
        else {
            mFundAdapter.setFunds(mFunds);
            mFundAdapter.notifyDataSetChanged();
        }

    }

}
