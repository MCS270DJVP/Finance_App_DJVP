package com.example.dennis.mutualfund;

import android.support.v4.app.Fragment;

public class FundActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new FundFragment();
    }
}
