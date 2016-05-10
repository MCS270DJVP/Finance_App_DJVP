package com.example.dennis.mutualfund;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.dennis.mutualfund.YahooFetchFragments.TaskFragment;

public class FundActivity extends SingleFragmentActivity implements TaskFragment.TaskCallbacks {
    private TaskFragment mTaskFragment;
    private FundFragment mFundFragment;
    private static final String TAG_TASK_FRAGMENT = "task_fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();
        mTaskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null) {
            mTaskFragment = new TaskFragment();
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }

    }

    @Override
    protected Fragment createFragment() {
        FundFragment ff = new FundFragment();
        return ff;
    }


    @Override
    public void onPreExecute() {

    }

    @Override
    public void onProgressUpdate(int percent) {

    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onPostExecute() {
        mFundFragment.enableCalc();
    }

    public void updateData() {
        mTaskFragment.fetchCalcData();
    }
}
