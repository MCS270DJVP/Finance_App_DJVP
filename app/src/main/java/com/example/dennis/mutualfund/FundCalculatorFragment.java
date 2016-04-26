package com.example.dennis.mutualfund;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FundCalculatorFragment extends Fragment {
    private List<Fund> mFunds;
    private static final String ARG_FUNDS = "FUNDS";

    private TextView mTextView0;
    private TextView mTextView1;
    private TextView mTextView2;
    List<TickerComparisonObject> objects = new ArrayList<TickerComparisonObject>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mResultRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFunds = FundLab.get(getActivity()).getFunds();
        /*I retrieve the data mFunds for you. This is where you do all the algorithm
        * as you need to. */
        // List of what is considered overweight and underweight.
        List<Fund> overweightx = new ArrayList<Fund>();
        List<Fund> underweighty = new ArrayList<Fund>();
        for(int i = 0; i < mFunds.size(); i++) {
            Fund fund = mFunds.get(i);
            if (fund.getWeight() == 0) {
                overweightx.add(fund);
            }
            if (fund.getWeight() == 1) {
                underweighty.add(fund);
            }
        }
        exchangeOptions(overweightx, underweighty);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*This is where you inflate the layout from */
        View v = inflater.inflate(R.layout.calculate_fragment, container, false);
        mResultRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_results);
        mResultRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mResultRecyclerView.setAdapter(new ComparisonAdapter(objects));
        return v;
    }

    public void exchangeOptions(List<Fund> overweights, List<Fund> underweights) {

        List<Double> tempOverweightValues;
        List<Double> tempUnderweightValues;

        for (int i = 0; i < overweights.size(); i++) {  // Each overweight
            for (int j = 0; j < underweights.size(); j++) {  // Each underweight
                tempOverweightValues = overweights.get(i).getHistoricalPrices();
                tempUnderweightValues = underweights.get(j).getHistoricalPrices();
                int attractiveValue = attractiveness(tempOverweightValues, tempUnderweightValues);  // Returns an int value
                objects.add(new TickerComparisonObject(overweights.get(i).getTicker(), underweights.get(j).getTicker(), attractiveValue));
            }
        }
        Collections.sort(objects, new likabilityComparator());  // Sort the objects
    }

    // Using a List because we are planning to be adding and removing a lot.
    public int attractiveness(List<Double> overweight, List<Double> underweight) {

        int ONE = 1;
        int THOUSAND = 1000;
        int ratioListSize = overweight.size();
        double[] ratioList = new double[ratioListSize];

        for (int i = 0; i < ratioListSize; i++) {
            Double a = overweight.get(i);
            Double b = underweight.get(i);
            // Divide with 3 digits after decimal
            Double ratio = (double) Math.round(a/b * THOUSAND) / THOUSAND;
            ratioList[i] = ratio;
        }

        // Holds the last ratio stored to be searched later
        double presentRatio = ratioList[ratioListSize - ONE];

        // Sorts the value in ascending order
        Arrays.sort(ratioList);

        // Returns the ranking of the attractiveness
        int ratioPosition = Arrays.binarySearch(ratioList, presentRatio) + ONE;
        return ratioPosition;
    }

    private class ComparisonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextView0;
        private TextView mTextView1;
        private TextView mTextView2;
        private TickerComparisonObject mTCO;

        public ComparisonHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTextView0 = (TextView) itemView.findViewById(R.id.list_item_ticker_textview0);
            mTextView1 = (TextView) itemView.findViewById(R.id.list_item_ticker_textview1);
            mTextView2 = (TextView) itemView.findViewById(R.id.list_item_ticker_textview2);
        }

        public void bindComparison(TickerComparisonObject tco){
            mTCO = tco;
            mTextView0.setText(mTCO.getOverweightTicker().toUpperCase());
            mTextView1.setText(mTCO.getUnderweightTicker().toUpperCase());
            mTextView2.setText(Integer.toString(mTCO.getlikabilityValue()));
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class ComparisonAdapter extends RecyclerView.Adapter<ComparisonHolder> {
        private List<TickerComparisonObject> mTCOs;
        public ComparisonAdapter(List<TickerComparisonObject> tcos) {
            mTCOs = tcos;
        }
        @Override
        public ComparisonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_result, parent, false);
            return new ComparisonHolder(view);
        }
        @Override
        public void onBindViewHolder(ComparisonHolder holder, int position){
            TickerComparisonObject tco = mTCOs.get(position);
            holder.bindComparison(tco);
        }
        @Override
        public int getItemCount() {
            return mTCOs.size();
        }
        /*if mFundAdapter is null then create a new mFundAdapter*/
        public void setTickerComparisonObjects(List<TickerComparisonObject> tcos) {mTCOs = tcos;}
    }

    class TickerComparisonObject{ // Could not think of a better name
        String overweightTicker;
        String underweightTicker;
        int likabilityValue;

        TickerComparisonObject(String owTicker, String uwTicker, int likValue){
            overweightTicker = owTicker;
            underweightTicker = uwTicker;
            likabilityValue = likValue;
        }

        public String getOverweightTicker(){
            return overweightTicker;
        }

        public String getUnderweightTicker(){
            return underweightTicker;
        }

        public int getlikabilityValue(){
            return likabilityValue;
        }
    }

    class likabilityComparator implements Comparator<TickerComparisonObject> {
        @Override
        public int compare(TickerComparisonObject a, TickerComparisonObject b) {
            // Sort the comparison from Biggest likability value to the least likability value.
            return a.likabilityValue > b.likabilityValue ? -1 : a.likabilityValue == b.likabilityValue ? 0 : 1;
        }
    }
}
