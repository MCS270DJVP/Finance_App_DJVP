package com.example.dennis.mutualfund.YahooFetch;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by huyviet1995 on 4/28/16.
 */
public class FetchData {

    private static final String TAG ="DATAFETCH";
    private String mLetter;
    public FetchData (String letter) {
        mLetter = letter;
    }

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
    public List<String> fetchItems() {
        List<String> items = new ArrayList<String>();
        try {
            String url = Uri.parse("http://d.yimg.com/aq/autoc")
                    .buildUpon()
                    .appendQueryParameter("query", mLetter)
                    .appendQueryParameter("region","US")
                    .appendQueryParameter("lang", "en-US")
                    .build().toString();
            String jsonString = getUrlString(url);
            JSONObject jsonbody = new JSONObject(jsonString);
            parseItems(items,jsonbody);
            Log.i(TAG, "Received JSON: " + jsonString);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"Receive symbol list: " + items);
        return items;
    }
    private void parseItems(List<String> items, JSONObject jsonBody)
            throws IOException, JSONException {
        JSONObject object = jsonBody.getJSONObject("ResultSet");
        JSONArray array = object.getJSONArray("Result");
        for (int i = 0; i < array.length();i++) {
            if (i>=3) {
                break;
            }
            JSONObject symbolObject = array.getJSONObject(i);
            items.add(symbolObject.getString("symbol"));
        }
    }

}
