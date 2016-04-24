package com.example.dennis.mutualfund.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.example.dennis.mutualfund.database.FundDBSchema.FundTable;
import com.example.dennis.mutualfund.Fund;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by macowner on 4/16/16.
 */
public class FundCursorWrapper extends CursorWrapper {
    public FundCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Fund getFund() {
        String uuidString = getString(getColumnIndex(FundTable.Cols.UUID));
        String ticker = getString(getColumnIndex(FundTable.Cols.TICKER));
        int weight = getInt(getColumnIndex(FundTable.Cols.WEIGHT));
        /*return the json object and convert it back to a list*/
        String hpricesString = getString(getColumnIndex(FundTable.Cols.HPRICES));

        long currentTime = getLong(getColumnIndex(FundTable.Cols.TIME));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);


        Gson gson = new Gson();
        List<Double> historicalPrices = gson.fromJson(hpricesString,ArrayList.class);

        Fund fund = new Fund(UUID.fromString(uuidString));
        fund.setTicker(ticker);
        fund.setWeight(weight);
        fund.setHistoricalPrices(historicalPrices);
        fund.setTime(calendar);
        return fund;
    }
}
