package com.example.dennis.mutualfund;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class FundCalculatorActivity extends SingleFragmentActivity{
    private static final String ARG_FUNDS = "FUND";
    public static Intent newIntent (Context packageContext) {
        Intent intent = new Intent (packageContext, FundCalculatorActivity.class);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        return new FundCalculatorFragment();
    }
}
