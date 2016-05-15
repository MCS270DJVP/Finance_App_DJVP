package com.example.dennis.mutualfund.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dennis.mutualfund.database.FundDBSchema.FundTable;

public class FundBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public FundBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + FundTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                FundTable.Cols.UUID + ", " +
                FundTable.Cols.STOCKPRICE+ ", "+
                FundTable.Cols.TICKER + ", " +
                FundTable.Cols.WEIGHT + ", " +
                FundTable.Cols.TIME + ", " +
                FundTable.Cols.HPRICES + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}