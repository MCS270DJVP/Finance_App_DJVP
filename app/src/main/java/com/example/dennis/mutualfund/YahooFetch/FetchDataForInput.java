package com.example.dennis.mutualfund.YahooFetch;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.ArrayList;
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_dropdown_item_1line, new FetchData(mInput).fetchItems());
        return adapter;
    }
    @Override
    protected void onPostExecute(ArrayAdapter<String> adapter) {
        mEditText.setAdapter(adapter);
    }
}
