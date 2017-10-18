package com.jayzh7.tma.Utils;

/**
 * This class is used to transform data of time to appropriate bar chart data
 * @author Jay
 * @since 10/8/2017.
 * @version 1.0
 */
public class ChartDataTransformer {

    private int[] mChartData;
    private int[] mEntryData;
    private int mMinTime;
    private int mMaxTime;

    /**
     * public constructor
     *
     * @param data the int array to be transformed
     */
    public ChartDataTransformer(int[] data) {
        mChartData = data;
        mEntryData = data;
    }

    /**
     * Transforme data and return
     * @return transformed data
     */
    public int[] getTransformedData() {
        transform(mChartData);
        return mEntryData;
    }

    public int getMin() {
        return mMinTime;
    }

    /**
     * Transform data so that the data set can be processed by bar chart.
     * @param mChartData
     */
    private void transform(int[] mChartData) {
        calcMinMax();
        for (int i = 2; i < mChartData[0]; i += 3) {
            mEntryData[i] = mChartData[i] - mMinTime;
            mEntryData[i + 1] = mChartData[i + 1] - mMinTime;
        }
    }

    /**
     * Get the max value and minimum value
     */
    private void calcMinMax() {
        mMinTime = 1440;
        mMaxTime = 0;

        for (int i = 2; i < mChartData[0]; i += 3) {
            calc(mChartData[i]);
            calc(mChartData[i + 1]);
        }
    }

    /**
     * Process a value
     * @param num value
     */
    private void calc(int num) {
        if (num < mMinTime) {
            mMinTime = num;
        }
        if (num > mMaxTime) {
            mMaxTime = num;
        }
    }
}
