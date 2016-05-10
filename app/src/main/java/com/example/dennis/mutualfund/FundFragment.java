package com.example.dennis.mutualfund;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.TextView;

import com.example.dennis.mutualfund.YahooFetch.FetchDataForAdd;
import com.example.dennis.mutualfund.YahooFetch.FetchDataForCalculate;
import com.example.dennis.mutualfund.YahooFetch.FetchDataForGraph;
import com.example.dennis.mutualfund.YahooFetch.FetchDataForInput;

import java.util.List;

public class FundFragment extends Fragment{
    private RecyclerView mFundRecyclerView;
    private AutoCompleteTextView mTickerField;
    private Button mCalculate;
    private static final String TAG = "MUTUAL_FUND";
    private FundAdapter mFundAdapter;
    private String mTickerTitle;
    private List<Fund> mFunds;
    private static final String KEY_SPINNERS = "spinners";
    private int[] savedWeights;


    @Override
    public void onCreate(Bundle savedInstancestate){
        super.onCreate(savedInstancestate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_fund, container, false);
        mFundRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mFundRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTickerField = (AutoCompleteTextView) v.findViewById(R.id.ticker_text);
        mTickerField.setThreshold(1);

        mTickerField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new FetchDataForInput(getActivity(),mTickerField.getText().toString().trim(),mTickerField).execute();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTickerField.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (isConnectedtoInternet()) {
                        mTickerTitle = mTickerField.getText().toString().trim().toUpperCase();
                    /* Checks for duplicate Ticker. Pops up dialog if Ticker already exists */
                        if (isRepeatString(mTickerTitle)) {
                            dialogMessage("Ticker already exists");
                        } else if (!isEmpty(mTickerField) && isValidString(mTickerTitle)) {
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
                            dialogMessage ("Invalid Ticker");
                        }
                    } else {
                        dialogMessage("No Internet access");
                    }
                    return true;
                }
                return false;
            }
        });


        mCalculate = (Button) v.findViewById(R.id.calculate_button);
        mCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectedtoInternet()) {
                    new FetchDataForCalculate(getActivity(), new Runnable() {
                        @Override
                        public void run() {
                        /*including the codes for the comparing recylcerView*/
                            Intent intent = FundCalculatorActivity.newIntent(getActivity());
                            startActivity(intent);
                        }
                    }).execute();
                }
                else
                    dialogMessage("No Internet access!");
            }
        });

        /* Stores Weight data from savedInstanceState for later use (for rotation) */
        if (savedInstanceState != null) {
            savedWeights = savedInstanceState.getIntArray(KEY_SPINNERS);
        }

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mFundAdapter.remove(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(mFundRecyclerView);

        updateUI();

        return v;
    }

    /* Stores Weight data to initialize spinners upon rotation */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        int[] spinners = new int[mFunds.size()];
        for (int i = 0; i < mFunds.size(); i++) {
            spinners[i] = (mFunds.get(i).getWeight());
        }
        savedInstanceState.putIntArray(KEY_SPINNERS, spinners);
    }

    private class FundHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Fund mFund;
        private TextView mTickerTextView;
        private Spinner mSpinner;
        public FundHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTickerTextView = (TextView) itemView.findViewById(R.id.list_item_ticker_textview);
            // Update Spinner
            mSpinner = (Spinner) itemView.findViewById(R.id.list_item_weight_spinner);
            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                    Object item = parent.getItemAtPosition(pos);
                    mFund.setWeight(pos);
                    FundLab.get(getActivity()).updateFund(mFund);
                }
                public void onNothingSelected(AdapterView<?> parent) {
                    return;
                }
            });

        }

        public void bindFund(Fund fund){
            mFund = fund;
            mSpinner.setSelection(mFund.getWeight());
            mTickerTextView.setText(mFund.getTicker().toUpperCase());
        }
        @Override
        public void onClick(View v) {
            if (!isConnectedtoInternet()) dialogMessage("No Internet Connection!");
            else
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


        public void remove(int adapterPosition) {
            FundLab.get(getActivity()).deleteFund(mFunds.get(adapterPosition));
            mFunds.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
        }
    }

    /*update user interface*/
    public void updateUI() {
        mFunds = FundLab.get(getActivity()).getFunds();
        if (mFundAdapter == null) {
            mFundAdapter = new FundAdapter(mFunds);
            mFundRecyclerView.setAdapter(mFundAdapter);
            /*Fetches data from the savedInstanceState (for rotation) */
            if (savedWeights != null) {
                for (int i = 0; i < mFunds.size() -1; i++) {
                    mFunds.get(i).setWeight(savedWeights[i]);
                }
            }
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

    /* Checks if a fund with the given Ticker exists in mFund */
    private boolean isRepeatString(String str) {
        for (int i = 0; i < mFunds.size();i++) {
            if (str.equals(mFunds.get(i).getTicker())) {
                return true;
            }
        }
        return false;
    }

    private void dialogMessage(String str) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(str);
        alert.setPositiveButton("OK",null);
        alert.show();
        mTickerField.setText("");
        updateUI();
    }
}