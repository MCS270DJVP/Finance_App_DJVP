package com.example.dennis.mutualfund;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.List;

public class GraphDialogFragment extends DialogFragment  {
    private static final String ARG_QUOTE = "QUOTE";
    private Fund mFund;
    private List<Double> mHistoricalPrices;

    public static GraphDialogFragment newInstance(Fund fund) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUOTE,fund);
        GraphDialogFragment fragment = new GraphDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Dialog onCreateDialog (Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.line_graph_view,null);
        mFund = (Fund) getArguments().getSerializable(ARG_QUOTE);
        mHistoricalPrices = mFund.getHistoricalPrices();

        GraphView graph = (GraphView) v.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        int count = 0;
        /*Looking for the max value of the mHistorical prices*/
        double max = 0;
        double min = mHistoricalPrices.get(0);
        for (int i = 0; i < mHistoricalPrices.size();i++) {
            if (mHistoricalPrices.get(i)!=null && mHistoricalPrices.get(i).doubleValue() >max)
                max = mHistoricalPrices.get(i).doubleValue();
            if (mHistoricalPrices.get(i)!=null && mHistoricalPrices.get(i).doubleValue() < min)
                min = mHistoricalPrices.get(i).doubleValue();
        }
        /*find the actual active days when stock market open*/
        int size = 0;
        for (int i = 0; i < mHistoricalPrices.size();i++) {
            if (mHistoricalPrices.get(i)!=null) {
                size++;
            }
        }
        for (int i = mHistoricalPrices.size()-1;i>=0;i--) {
            if (mHistoricalPrices.get(i) !=null) {
                series.appendData(new DataPoint(count,mHistoricalPrices.get(i)),true,size);
                count++;
            }
        }
        series.setColor(Color.parseColor("#00BCD4"));
        graph.addSeries(series);
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(min);
        viewport.setMaxY(max);
        viewport.setXAxisBoundsManual(true);
        viewport.setScrollable(true);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Historical quotes for " + mFund.getTicker())
                .setPositiveButton(R.string.positive_button,null)
                .create();
    }

}
