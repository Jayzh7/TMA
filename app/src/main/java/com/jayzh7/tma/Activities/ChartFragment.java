package com.jayzh7.tma.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.jayzh7.tma.Database.DatabaseHelper;
import com.jayzh7.tma.R;
import com.jayzh7.tma.Utils.ChartDataTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 10/14/2017.
 */

public class ChartFragment extends android.support.v4.app.Fragment {

    public static final int sREQUEST_CODE = 1;

    private HorizontalBarChart mChart;
    private DatabaseHelper mDB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chart_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        FloatingActionButton fab = getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addEventIntent = new Intent(getActivity(), AddEventActivity.class);
                startActivityForResult(addEventIntent, sREQUEST_CODE);
            }
        });

        mDB = DatabaseHelper.getInstance(getActivity());
        mChart = view.findViewById(R.id.chart);
        if (mChart != null) {
            setChart();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setChart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up butto n, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_clear_all) {
            mDB = DatabaseHelper.getInstance(getActivity());
            mDB.clearAll();
            setChart();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setChart() {
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);
        mChart.getDescription().setEnabled(false);

        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(true);
        mChart.setDoubleTapToZoomEnabled(false);

        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerTapEnabled(true);

        setData();
        mChart.setFitBars(true);
        mChart.animateY(2500);
        mChart.setHighlightFullBarEnabled(false);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
    }

    private void setData() {
        float barWidth = 1f;
        float spaceForBar = 1f;

        List<BarEntry> entries = new ArrayList<>();
        int[] chartData = mDB.readForBarCharts();

        ChartDataTransformer transformer = new ChartDataTransformer(chartData);
        int[] entryData = transformer.getTransformedData();

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);

        YAxis yl = mChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.resetAxisMinimum();
        yl.setTimeOffset(transformer.getMin());
        yl.setAxisMinimum(0f);

        YAxis yr = mChart.getAxisRight();
        yr.setTimeOffset(transformer.getMin());
        yr.setDrawAxisLine(false);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        for (int i = 2, j = 0; i < entryData[0]; i += 3, j++) {
            entries.add(new BarEntry(
                    entryData[i - 1],                   // ID
                    (i + 1) / 3,                     // display order
                    entryData[i],                       // Start time (transformed)
                    entryData[i + 1] - entryData[i], // End time   (transformed)
                    getResources().getDrawable(R.drawable.side_nav_bar)));
        }

        BarDataSet set;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set.setValues(entries);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set = new BarDataSet(entries, "Dataset 1");

            set.setDrawIcons(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(barWidth);
            mChart.setData(data);
        }
    }

}
