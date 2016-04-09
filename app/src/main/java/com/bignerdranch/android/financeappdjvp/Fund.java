package com.bignerdranch.android.financeappdjvp;

import java.util.UUID;

/**
 * Created by huyviet1995 on 4/8/16.
 */
public class Fund {
    private String mTitle;
    private UUID mId;
    //Questions: Properties of a fund
    public Fund(String title) {
        mTitle = title;
    }
    //get Id
    public UUID getId() {
        return mId;
    }
    //set Id
    public void setId(UUID newId) {
        mId = newId;
    }
    //get name of the fund
    /*Dennis: change the getTitle to getTicker*/
    public String getTitle() {
        return mTitle;
    }
    //set title of the fund
    public void setTitle(String newTitle) {
        mTitle = newTitle;
    }
}
