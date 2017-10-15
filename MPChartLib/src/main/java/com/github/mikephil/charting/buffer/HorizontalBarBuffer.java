
package com.github.mikephil.charting.buffer;

import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class HorizontalBarBuffer extends BarBuffer {

    private int[] mBarId;
    private float[][] mBarPos;

    public HorizontalBarBuffer(int size, int dataSetCount, boolean containsStacks) {
        super(size, dataSetCount, containsStacks);

        mBarId = new int[60];
        mBarPos = new float[60][4];
    }

    public float[][] getBarPos() {
        return mBarPos;
    }

    @Override
    public void feed(IBarDataSet data) {

        float size = data.getEntryCount() * phaseX;
        float barWidthHalf = mBarWidth / 2f;

        Log.d("HBB3", "out of for");
        for (int i = 0; i < size; i++) {
            BarEntry e = data.getEntryForIndex(i);

            if (e == null)
                continue;

            float x = e.getX();
            float y = e.getY();
            float[] vals = e.getYVals();
            int id = e.getID(); // ID of the entry

            if (!mContainsStacks || vals == null) {

                float bottom = x - barWidthHalf;
                float top = x + barWidthHalf;
                float left, right;
                if (mInverted) {
                    left = y >= 0 ? y : 0;
                    right = y <= 0 ? y : 0;
                } else {
                    left = e.getOffset();
                    right = e.getY();
                    /** original code
                     right = y >= 0 ? y : 0;
                     left = y <= 0 ? y : 0;
                     */
                }

                if (right > 0)
                    right *= phaseY;
                else
                    left *= phaseY;
                Log.d("HBB3", "ID:" + id + "LEFT:" + left + " TOP:" + top + " RIGHT:" + String.valueOf(right) + " BOTTOM:" + bottom);

                // Don'r animate from zero to start
                if (right > left) {
                    addBar(left, top, right, bottom);
                    mBarPos[id][0] = left;
                    mBarPos[id][1] = top;
                    mBarPos[id][2] = right;
                    mBarPos[id][3] = bottom;
                }
            } else {

                float posY = 0f;
                float negY = -e.getNegativeSum();
                float yStart = 0f;

                // fill the stack
                for (int k = 0; k < vals.length; k++) {

                    float value = vals[k];

                    if (value >= 0f) {
                        y = posY;
                        yStart = posY + value;
                        posY = yStart;
                    } else {
                        y = negY;
                        yStart = negY + Math.abs(value);
                        negY += Math.abs(value);
                    }

                    float bottom = x - barWidthHalf;
                    float top = x + barWidthHalf;
                    float left, right;
                    if (mInverted) {
                        left = y >= yStart ? y : yStart;
                        right = y <= yStart ? y : yStart;
                    } else {
                        right = y >= yStart ? y : yStart;
                        left = y <= yStart ? y : yStart;
                    }

                    // multiply the height of the rect with the phase
                    right *= phaseY;
                    left *= phaseY;
                    addBar(left, top, right, bottom);
                }
            }
        }
        reset();
    }
}
