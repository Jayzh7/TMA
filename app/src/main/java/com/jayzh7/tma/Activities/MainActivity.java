package com.jayzh7.tma.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BaseDataSet;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.jayzh7.tma.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static final int sREQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addEventIntent = new Intent(MainActivity.this, AddEventActivity.class);
//                createIntent.putExtra()
                startActivityForResult(addEventIntent, sREQUEST_CODE);
            }
        });
        HorizontalBarChart chart = findViewById(R.id.chart);
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 200f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        entries.add(new BarEntry(4f, 70f));
        entries.add(new BarEntry(5f, 60f));
        entries.add(new BarEntry(6f, 60f));
        entries.add(new BarEntry(7f, 60f));
        entries.add(new BarEntry(8f, 60f));
        entries.add(new BarEntry(9f, 60f));

        BarDataSet set = new BarDataSet(entries, "BarDataSet");

        BarData data = new BarData(set);
        data.setBarWidth(0.6f);
        chart.setData(data);
        chart.setFitBars(true);
        chart.invalidate();
        chart.setMaxVisibleValueCount(100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up butto n, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CustomBarDataSet extends BaseDataSet<BarEntry> implements IBarDataSet {

        @Override
        public boolean isStacked() {
            return false;
        }

        @Override
        public int getStackSize() {
            return 0;
        }

        @Override
        public int getBarShadowColor() {
            return 0;
        }

        @Override
        public float getBarBorderWidth() {
            return 0;
        }

        @Override
        public int getBarBorderColor() {
            return 0;
        }

        @Override
        public int getHighLightAlpha() {
            return 0;
        }

        @Override
        public String[] getStackLabels() {
            return new String[0];
        }

        @Override
        public int getHighLightColor() {
            return 0;
        }

        @Override
        public float getYMin() {
            return 0;
        }

        @Override
        public float getYMax() {
            return 0;
        }

        @Override
        public float getXMin() {
            return 0;
        }

        @Override
        public float getXMax() {
            return 0;
        }

        @Override
        public int getEntryCount() {
            return 0;
        }

        @Override
        public void calcMinMax() {

        }

        @Override
        public void calcMinMaxY(float fromX, float toX) {

        }

        @Override
        public BarEntry getEntryForXValue(float xValue, float closestToY, DataSet.Rounding rounding) {
            return null;
        }

        @Override
        public BarEntry getEntryForXValue(float xValue, float closestToY) {
            return null;
        }

        @Override
        public List<BarEntry> getEntriesForXValue(float xValue) {
            return null;
        }

        @Override
        public BarEntry getEntryForIndex(int index) {
            return null;
        }

        @Override
        public int getEntryIndex(float xValue, float closestToY, DataSet.Rounding rounding) {
            return 0;
        }

        @Override
        public int getEntryIndex(BarEntry e) {
            return 0;
        }

        @Override
        public boolean addEntry(BarEntry e) {
            return false;
        }

        @Override
        public void addEntryOrdered(BarEntry e) {

        }

        @Override
        public boolean removeEntry(BarEntry e) {
            return false;
        }

        @Override
        public void clear() {

        }
    }
}

