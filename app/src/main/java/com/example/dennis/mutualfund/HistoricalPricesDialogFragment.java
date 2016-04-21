package com.example.dennis.mutualfund;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;

import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by huyviet1995 on 4/21/16.
 */
public class HistoricalPricesDialogFragment extends DialogFragment  {
    private RecyclerView mDialogRecyclerView;
    private TextView mQuotesTextView;
    private static final String ARG_QUOTE = "QUOTE";
    private Fund mFund;
    private DialogAdapter mDialogAdapter;

    public static HistoricalPricesDialogFragment newInstance(Fund fund) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUOTE,fund);
        HistoricalPricesDialogFragment fragment = new HistoricalPricesDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Dialog onCreateDialog (Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog,null);
        mDialogRecyclerView = (RecyclerView) v.findViewById(R.id.dialog_fragment);
        mDialogRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Historical quotes")
                .setPositiveButton(R.string.positive_button,null)
                .create();
    }

    private class DialogHolder extends RecyclerView.ViewHolder {
        private HistoricalQuote mQuote;
        public DialogHolder(View itemView) {
            super(itemView);
            mQuotesTextView = (TextView) itemView.findViewById(R.id.list_item_dialog_fragment);
        }

        public void bindQuote(HistoricalQuote quote) {
            mQuote = quote;
            if (mQuote != null) {
                mQuotesTextView.setText(mQuote.toString());
            }
        }
    }
    private class DialogAdapter extends RecyclerView.Adapter<DialogHolder> {
        private List<HistoricalQuote> mHistoricalQuotes;
        public DialogAdapter (List<HistoricalQuote> quotes) {
            mHistoricalQuotes = quotes;
        }
        @Override
        public DialogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_fragment_dialog, parent, false);
            return new DialogHolder(view);
        }

        @Override
        public void onBindViewHolder(DialogHolder holder, int position) {
            HistoricalQuote quote = mHistoricalQuotes.get(position);
            holder.bindQuote(quote);
        }

        @Override
        public int getItemCount() {
            return mHistoricalQuotes.size();
        }

        public void setFund(Fund fund) {
            mFund = fund;
        }
    }

    public void updateUI() {
        mFund = (Fund) getArguments().getSerializable(ARG_QUOTE);
        if (mDialogAdapter == null) {
            mDialogAdapter = new DialogAdapter(mFund.getHistoricalQuotes());
            mDialogRecyclerView.setAdapter(mDialogAdapter);
        }
        else {
            mDialogAdapter.setFund(mFund);
            mDialogAdapter.notifyDataSetChanged();
        }
        mDialogRecyclerView.setAdapter(mDialogAdapter);
    }
}
