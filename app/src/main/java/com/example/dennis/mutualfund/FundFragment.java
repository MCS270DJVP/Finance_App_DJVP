package com.example.dennis.mutualfund;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FundFragment extends Fragment{
    private Fund mFund;

    private RecyclerView mFundRecyclerView;
    private FundAdapter mAdapter;

    private EditText mTickerField;
    private Button mAddButton;
    private Button mCalculate;

    @Override
    public void onCreate(Bundle savedInstancestate){
        super.onCreate(savedInstancestate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_fund, container, false);

        mFundRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mFundRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //THE ADD FUNCTION BEING ABLE TO GET THE TICKER FIELD INFORMATION
        mTickerField = (EditText) v.findViewById(R.id.ticker_text);
        mAddButton = (Button) v.findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Fund x = new Fund();
                x.setTicker(mTickerField.getText().toString());
                FundLab.get(getActivity()).addFund(x);
                mTickerField.setText("");
                updateUI();
            }
        });

        mCalculate = (Button) v.findViewById(R.id.calculate_button);
        //mCalculate.setEnabled(false);

        updateUI();

        return v;
    }

    private void updateUI() {
        FundLab fundLab = FundLab.get(getActivity());
        List<Fund> funds = fundLab.getFunds();

        mAdapter = new FundAdapter(funds);
        mFundRecyclerView.setAdapter(mAdapter);
    }


    private class FundHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Fund mFund;

        private TextView mTickerTextView;
        //private TextView mStockTextView; //TESTING ONLY
        private Spinner mWeightScrollerView;

        public FundHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            mTickerTextView = (TextView) itemView.findViewById(R.id.list_item_ticker_textview);
            //mStockTextView = (TextView) itemView.findViewById(R.id.list_item_stock_value_textview); //TESTING ONLY
            //mWeightScrollerView = (Spinner) itemView.findViewById(R.id.list_item_weight_spinner);
        }

        public void bindFund(Fund fund){
            mFund = fund;
            mTickerTextView.setText(mFund.getTicker());
            //mStockTextView.setText(0);
        }

        @Override
        public void onClick(View v){
            Toast.makeText(getActivity(),
                    mFund.getTicker() + " Clicked!", Toast.LENGTH_SHORT).show();
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
            View view = layoutInflater
                    .inflate(R.layout.list_item_fund, parent, false);
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
    }
}
