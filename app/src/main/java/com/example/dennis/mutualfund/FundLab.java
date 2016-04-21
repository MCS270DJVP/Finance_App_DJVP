package com.example.dennis.mutualfund;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Environment;

        import com.example.dennis.mutualfund.database.FundBaseHelper;
        import com.example.dennis.mutualfund.database.FundCursorWrapper;
        import com.example.dennis.mutualfund.database.FundDBSchema.FundTable;

        import java.io.File;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.UUID;

/**
 * Created by macowner on 3/6/16.
 */
public class FundLab {
    private static FundLab sFundLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static FundLab get (Context context) {
        if (sFundLab == null) {
            sFundLab = new FundLab(context);
        }
        return sFundLab;
    }

    private FundLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new FundBaseHelper(mContext)
                .getWritableDatabase();

    }

    /* The function we call every time we update our UI */
    public List<Fund> getFunds() {
        List<Fund> funds = new ArrayList<>();

        FundCursorWrapper cursor = queryFunds(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                funds.add(cursor.getFund());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return funds;
    }

    public void addFund(Fund c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(FundTable.NAME, null, values);
    }

    public void deleteFund(Fund c) {
        mDatabase.delete(FundTable.NAME, FundTable.Cols.UUID + " = ?",
                new String[]{c.getId().toString()});
    }
    /*Not currently useful, but I'm leaving it in here for now*/
    public Fund getFund(UUID id) {
        FundCursorWrapper cursor = queryFunds(
                FundTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getFund();
        } finally {
            cursor.close();
        }
    }

    /*Not currently useful, but I'm leaving it in here for now*/
    public void updateFund(Fund fund) {
        String uuidString = fund.getId().toString();
        ContentValues values = getContentValues(fund);

        mDatabase.update(FundTable.NAME, values,
                FundTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    private static ContentValues getContentValues(Fund fund) {
        ContentValues values = new ContentValues();
        values.put(FundTable.Cols.UUID, fund.getId().toString());
        values.put(FundTable.Cols.TICKER, fund.getTicker());
        values.put(FundTable.Cols.WEIGHT, fund.getWeight());

        return values;
    }

    private FundCursorWrapper queryFunds(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                FundTable.NAME,
                null, //Columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return new FundCursorWrapper(cursor);
    }
}
