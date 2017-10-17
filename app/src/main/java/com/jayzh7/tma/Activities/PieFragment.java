package com.jayzh7.tma.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jayzh7.tma.Database.DatabaseHelper;
import com.jayzh7.tma.R;

import java.util.ArrayList;

/**
 * Fragment that displays a pie chart shows time distribution on different type of events.
 * A simple {@link Fragment} subclass.
 */
public class PieFragment extends Fragment {

    PieChart mChart;

    public PieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper.getInstance(getActivity()).readForPieChart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pie, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mChart = view.findViewById(R.id.pieChart);

        setChart();
    }

    /**
     * Sets attributes and data for pie chart
     */
    private void setChart() {
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setHighlightPerTapEnabled(true);

        setDataForPieChart();
    }

    /**
     * Sets data for pie chart
     */
    private void setDataForPieChart() {
        ArrayList<PieEntry> entries = DatabaseHelper.getInstance(getActivity()).readForPieChart();
        PieDataSet dataSet = new PieDataSet(entries, "Statistics");

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);

        mChart.setData(data);
        mChart.highlightValues(null);

        mChart.invalidate();
    }
}
