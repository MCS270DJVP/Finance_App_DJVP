package com.example.dennis.mutualfund.YahooFetch;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by huyviet1995 on 4/28/16.
 */
public class FetchDataForInput extends AsyncTask<String,Void,ArrayAdapter<String>> {
    private String mInput;
    private Context mContext;
    private AutoCompleteTextView mEditText;
    private Map<String,String> map;
    public FetchDataForInput(Context context, String input, AutoCompleteTextView editText) {
        mInput = input;
        mContext = context;
        mEditText = editText;
    }
    @Override
    protected ArrayAdapter<String> doInBackground(String... params) {
        map = new FetchData(mInput).fetchItems();
        List<String> symbolList = new ArrayList<String>();
        List<String> nameList = new ArrayList<String>();
        if (mInput.length()<=5) {
            for (Map.Entry<String,String> entry: map.entrySet()) {
                symbolList.add(entry.getValue());
            }
        }
        else
            for (Map.Entry<String,String> entry : map.entrySet()) {
                symbolList.add(entry.getKey());
            }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_dropdown_item_1line,symbolList) ;
        Log.i("Test","Received adapter" + adapter);
        return adapter;
    }
    @Override
    protected void onPostExecute(ArrayAdapter<String> adapter) {
        mEditText.setAdapter(adapter);
        mEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = map.get(parent.getItemAtPosition(position).toString());
                if (selectedItem!=null)
                    mEditText.setText(selectedItem);
                else
                    mEditText.setText(parent.getItemAtPosition(position).toString());
            }
        });
        adapter.notifyDataSetChanged();
    }
}
