package com.bignerdranch.android.financeappdjvp;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by huyviet1995 on 4/8/16.
 */
public class Fund {
    private String mTitle;
    private UUID mId;
    private BigDecimal mPrice;

    public Fund(String title) {
        mTitle = title;
    }

    public BigDecimal getPrice() {
        return mPrice;
    }

    public void setPrice(BigDecimal mPrice) {
        this.mPrice = mPrice;
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
    public String getTitle() {
        return mTitle;
    }
    //set title of the fund
    public void setTitle(String newTitle) {
        mTitle = newTitle;
    }


}
