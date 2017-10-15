package com.jayzh7.tma.Utils;

import org.joda.time.DateTime;

/**
 * Created by Jay on 10/15/2017.
 */

public class DateTimeConverter {
    private DateTime mTime;
    private int mHour;
    private int mMinute;

    public DateTimeConverter(DateTime time) {
        mTime = time;
    }

    public DateTimeConverter(int hour, int minute) {
        mHour = hour;
        mMinute = minute;
    }

    public DateTimeConverter(int minutes) {
        mHour = minutes / 60;
        mMinute = minutes % 60;
    }

    public String getConvertedTime() {
        if (mHour < 10) {
            if (mMinute < 10)
                return "0" + mHour + ":0" + mMinute;
            else
                return "0" + mHour + ":" + mMinute;
        } else {
            if (mMinute < 10)
                return "" + mHour + ":0" + mMinute;
            else
                return mHour + ":" + mMinute;
        }
    }

    public int getMinutes() {
        return mHour * 60 + mMinute;
    }
}
