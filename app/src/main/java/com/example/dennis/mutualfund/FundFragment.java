package com.example.dennis.mutualfund;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dennis.mutualfund.YahooFetch.FetchDataForAdd;
import com.example.dennis.mutualfund.YahooFetch.FetchDataForCalculate;
import com.example.dennis.mutualfund.YahooFetch.FetchDataForGraph;

import java.math.BigDecimal;
import java.util.List;

public class FundFragment extends Fragment{
    private Fund mFund;
    private RecyclerView mFundRecyclerView;
    private EditText mTickerField;
    private TextView mPriceField;
    private Button mAddButton;
    private ImageButton mRemoveButton;
    private Button mCalculate;
    private static final String TAG = "MUTUAL_FUND";
    private FundAdapter mFundAdapter;
    private BigDecimal mStockPrice;
    private String mTickerTitle;
    private List<Fund> mFunds;
    private Spinner mSpinner;


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
                if (isConnectedtoInternet()) {
                    mTickerTitle = mTickerField.getText().toString().trim();
                    if (!isEmpty(mTickerField) && isValidString(mTickerTitle)) {
                        new FetchDataForAdd(getActivity(), mTickerTitle,
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        updateUI();
                                    }
                                }
                        ).execute();
                        mTickerField.setText("");

                    } else if (!isValidString(mTickerTitle)){
                        mTickerField.setText("");
                        updateUI();
                    }
                }
            }

        });
        mCalculate = (Button) v.findViewById(R.id.calculate_button);
        mCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchDataForCalculate(getActivity(), new Runnable() {
                    @Override
                    public void run() {
                        /*including the codes for the comparing recylcerView*/

                    }
                }).execute();
            }
        });
        updateUI();
        return v;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("SPINNER",mSpinner.getSelectedItemPosition());
    }

    private class FundHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Fund mFund;
        private TextView mTickerTextView;
        private Spinner mWeightScrollerView;
        public FundHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTickerTextView = (TextView) itemView.findViewById(R.id.list_item_ticker_textview);
            mPriceField = (TextView) itemView.findViewById(R.id.price_display);
            mRemoveButton = (ImageButton) itemView.findViewById(R.id.remove);
            
            mSpinner = (Spinner) itemView.findViewById(R.id.list_item_weight_spinner);
            mRemoveButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    if (mFund != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Deletion Alert");
                        builder.setMessage("Do you really want to delete this?");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "Fund Deleted!", Toast.LENGTH_SHORT).show();
                                FundLab.get(getActivity()).deleteFund(mFund);
                                updateUI();
                            }
                        });
                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                }
            });
        }

        public void bindFund(Fund fund){
            mFund = fund;
            mTickerTextView.setText(mFund.getTicker().toUpperCase());
            if (mFund.getStockValue()!=null ) {
                mPriceField.setText(mFund.getStockValue().toString());
            }
        }
        @Override
        public void onClick(View v) {
            new FetchDataForGraph(getActivity(),getFragmentManager(),mFund).execute();
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

    /*update user interface*/
    public void updateUI() {
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
    /*check if there is internet connection*/
    private boolean isConnectedtoInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo !=null &&  activeNetworkInfo.isConnected();
    }
    /*check if the string contains no white space*/
    private boolean isValidString(String str) {
        for (int i = 0; i < str.length();i++) {
            if (str.charAt(i) == ' ') {
                return false;
            }
        }
        return true;
    }
    /*check if the input ticker is empty*/
    private boolean isEmpty (EditText text) {
        if (text.getText().toString().length() > 0) {
            return false;
        }
        else return true;
    }
    /*if the ticker is invalid, pop up a dialog noticing about invalid ticker*/


}