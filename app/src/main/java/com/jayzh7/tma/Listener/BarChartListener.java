package com.jayzh7.tma.Listener;

import android.view.MotionEvent;
import android.view.View;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.listener.ChartTouchListener;

/**
 * Created by Jay on 10/8/2017.
 */

public class BarChartListener extends ChartTouchListener {

    public BarChartListener(Chart chart) {
        super(chart);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
