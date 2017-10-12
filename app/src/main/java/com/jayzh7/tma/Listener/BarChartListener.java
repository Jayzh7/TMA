package com.jayzh7.tma.Listener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.jayzh7.tma.Activities.AddEventActivity;

/**
 * Created by Jay on 10/8/2017.
 */

public class BarChartListener implements OnChartGestureListener {

    private Context mContext;
    private HorizontalBarChart mChart;

    public BarChartListener(Context context) {
        mContext = context;
    }

    public BarChartListener(Context context, HorizontalBarChart chart) {
        mContext = context;
        mChart = chart;
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Intent intent = new Intent(mContext, AddEventActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.d("MYLISTENER", "single tap");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }
}
