package com.example.dennis.mutualfund;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FundLab {
    private static FundLab sFundLab;

    private List<Fund> mFunds;

    public static FundLab get(Context context){
        if (sFundLab == null) {
            sFundLab = new FundLab(context);
        }
        return sFundLab;
    }

    private FundLab(Context context){
        mFunds = new ArrayList<>();
    }

    public List<Fund> getFunds() {
        return mFunds;
    }

    public Fund getFund(UUID id) {
        for(Fund fund : mFunds){
            if(fund.getId().equals(id)){
                return fund;
            }
        }
        return null;
    }
    public void addFund(Fund a){
        mFunds.add(a);
    }

    public void deleteFund(Fund a){
        mFunds.remove(a);
    }
}
