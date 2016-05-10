package com.example.dennis.mutualfund.YahooFetch;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by huyviet1995 on 4/28/16.
 */
public class FetchDataForInput extends AsyncTask<String,Void,ArrayAdapter<String>> {
    private String mInput;
    private Context mContext;
    private AutoCompleteTextView mEditText;
    public FetchDataForInput(Context context, String input, AutoCompleteTextView editText) {
        mInput = input;
        mContext = context;
        mEditText = editText;
    }
    @Override
    protected ArrayAdapter<String> doInBackground(String... params) {

        ArrayAdapter<String> adapter = null;
        if (mContext != null) {
            adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, new FetchData(mInput).fetchItems());
            Log.i("Test", "Received adapter" + adapter);
            //return adapter;
        } else {
            this.cancel(true);
        }
        return adapter;
    }
    @Override
    protected void onPostExecute(ArrayAdapter<String> adapter) {
        if (adapter != null) {
            mEditText.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
