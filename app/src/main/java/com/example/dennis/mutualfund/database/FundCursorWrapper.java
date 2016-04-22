package com.example.dennis.mutualfund.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import java.util.Date;
import java.util.UUID;
import com.example.dennis.mutualfund.database.FundDBSchema.FundTable;
import com.example.dennis.mutualfund.Fund;

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

        Fund fund = new Fund(UUID.fromString(uuidString));
        fund.setTicker(ticker);
        fund.setWeight(weight);

        return fund;
    }
}
