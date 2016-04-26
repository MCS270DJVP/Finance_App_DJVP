package com.example.dennis.mutualfund;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huyviet1995 on 4/26/16.
 */
public class FundCalculatorActivity extends SingleFragmentActivity{
    private static final String ARG_FUNDS = "FUND";
    private List<Fund> mFunds;
    public static Intent newIntent (Context packageContext) {
        Intent intent = new Intent (packageContext, FundCalculatorActivity.class);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        return new FundCalculatorFragment();
    }
}
