package com.bignerdranch.android.financeappdjvp;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This will store all funds into one list
 */
public class ListOfFunds {
    private static ListOfFunds sFundList;
    private ArrayList<Fund> mFunds;
    /*To get the context for the fund list*/
    public static ListOfFunds get(Context context) {
        if (sFundList == null) {
            sFundList = new ListOfFunds(context);
        }
        return sFundList;
    }
    /*Construct an empty arrayList*/
    private ListOfFunds(Context context) {
        mFunds = new ArrayList<>();
        //This piece of code create a random list of funds only for testing
        /*for (int i = 0; i < 50; i++) {
            Fund fund = new Fund("Fund " + i);
            mFunds.add(fund);
        }*/
        //End of testing
    }
    /*Get list of funds*/
    public ArrayList<Fund> getFunds () {
        return mFunds;
    }

    /*Add the fund*/
    public void addFund(Fund fund) {
        mFunds.add(fund);
    }
    /*Get the fund with the specified UUID from the list*/
    public Fund getFund(UUID id) {
        for (Fund fund:mFunds) {
            if (fund.getId()==id) {
                return fund;
            }
        }
        return null;
    }

}
