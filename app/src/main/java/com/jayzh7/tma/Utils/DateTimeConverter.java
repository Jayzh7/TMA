package com.jayzh7.tma.Utils;

import org.joda.time.DateTime;

/**
 * Convert a DateTime object to a string to display on the screen
 * @author Jay
 * @since 10/15/2017.
 * @version 1.0
 */
public class DateTimeConverter {
    private DateTime mTime;
    private int mHour;
    private int mMinute;

    /**
     * Constructor
     *
     * @param time DateTime object
     */
    public DateTimeConverter(DateTime time) {
        mTime = time;
    }

    /**
     * Constructor
     * @param hour hour
     * @param minute minute
     */
    public DateTimeConverter(int hour, int minute) {
        mHour = hour;
        mMinute = minute;
    }

    /**
     * Constructor
     * @param minutes time in minutes
     */
    public DateTimeConverter(int minutes) {
        mHour = minutes / 60;
        mMinute = minutes % 60;
    }

    /**
     * Convert time to display format
     * @return display format string
     */
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
