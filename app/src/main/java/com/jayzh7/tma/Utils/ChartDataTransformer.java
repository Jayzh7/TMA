package com.jayzh7.tma.Utils;

/**
 * This class is used to transform data of time to appropriate bar chart data
 * <p>
 * Created by Jay on 10/8/2017.
 */

public class ChartDataTransformer {

    private int[] mChartData;
    private int[] mEntryData;
    private int mMinTime;
    private int mMaxTime;

    public ChartDataTransformer(int[] data) {
        mChartData = data;
        mEntryData = data;
    }

    public int[] getTransformedData() {
        transform(mChartData);
        return mEntryData;
    }

    private void transform(int[] mChartData) {
        calcMinMax();
        for (int i = 2; i < mChartData[0]; i += 3) {
            mEntryData[i] = mChartData[i] - mMinTime;
            mEntryData[i + 1] = mChartData[i + 1] - mMinTime;
        }
    }

    private void calcMinMax() {
        mMinTime = 1440;
        mMaxTime = 0;

        for (int i = 2; i < mChartData[0]; i += 3) {
            calc(mChartData[i]);
            calc(mChartData[i + 1]);
        }
    }

    private void calc(int num) {
        if (num < mMinTime) {
            mMinTime = num;
        }
        if (num > mMaxTime) {
            mMaxTime = num;
        }
    }
}
