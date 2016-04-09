package com.bignerdranch.android.financeappdjvp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huyviet1995 on 4/8/16.
 */
public class FinanceMainFragment extends Fragment{
    private Fund mFund;
    private RecyclerView mRecyclerView;
    private ArrayList<Fund> mFunds;
    private FundAdapter mFundAdapter;
    private TextView mTitleView;

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
        View view = inflater.inflate(R.layout.fragment_main_screen,container,false);
        /*get the recycler view from fragment_main_screen*/
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fund_recycler_view);
        /*set the layout for the recycler view*/
        updateUI();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
    /*update user interface*/
    private void updateUI() {
        /*Get all the funds from the list of funds*/
        mFunds = ListOfFunds.get(getActivity()).getFunds();
        /*Initialize the adapter*/
        mFundAdapter = new FundAdapter(mFunds);
        /*set the fund adapter to the recyclerView*/
        mRecyclerView.setAdapter(mFundAdapter);
    }

    /*Design each list of item on the fund view*/
    public class FundHolder extends RecyclerView.ViewHolder {
        /*Constructor of the fund holder*/
        public FundHolder(View itemView) {
            super(itemView);
            /*Set the name of the stock and the button right here (Dennis and Peter)*/
            mTitleView = (TextView) itemView.findViewById(R.id.list_item_fund);
        }
        /*Binding the fund*/
        public void bindFund(Fund fund) {
            mFund = fund;
            /*set the name and the title of the text view*/
            mTitleView.setText(mFund.getTitle());
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
            /*Ordering the funds based on its position in the list of funds*/
            Fund fund = mFunds.get(position);
            holder.bindFund(fund);
        }

        @Override
        public int getItemCount() {
            return mFunds.size();
        }
    }

}
