package com.example.dennis.mutualfund.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dennis.mutualfund.database.FundDBSchema.FundTable;

/**
 * Created by macowner on 4/16/16.
 */
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
                FundTable.Cols.TICKER + ", " +
                FundTable.Cols.WEIGHT + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}