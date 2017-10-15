package com.jayzh7.tma.Utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import org.joda.time.DateTime;

/**
 * Created by Jay on 10/3/2017.
 */

public class MyTimePicker {

    private TextView mTextView;
    private DateTime mTime;
    private Context mContext;
    private boolean validity;

    public MyTimePicker(Context context, TextView textView) {
        mContext = context;
        mTextView = textView;
        mTime = new DateTime();
        setListener();
        validity = false;
    }

    /**
     * Set up listener for start time TV and end time TV
     */
    private void setListener() {
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        mTextView.setText(new DateTimeConverter(hour, minute).getConvertedTime());
                        mTime = new DateTime(mTime.getYear(), mTime.getMonthOfYear(), mTime.getDayOfMonth(), hour, minute, 0, 0);
                    }
                }, mTime.getHourOfDay(), mTime.getMinuteOfHour(), true);
                validity = true;
                timePickerDialog.show();
            }
        });
    }

    public void testInput(int hour, int minute) {
        mTime = new DateTime(2017, 10, 8, hour, minute, 0, 0);
        validity = true;
    }

    public void setTime(int hour, int minute) {
        mTime = new DateTime(mTime.getYear(), mTime.getMonthOfYear(), mTime.getDayOfMonth(), hour, minute, 0, 0);
        validity = true;
    }

    public void setTime(DateTime time) {
        mTime = time;
        validity = true;
    }

    public void setTime(int minutes) {
        mTime = new DateTime(mTime.getYear(), mTime.getMonthOfYear(), mTime.getDayOfMonth(), minutes / 60, minutes % 60, 0, 0);
        validity = true;
    }

    public DateTime getDateTime() {
        return mTime;
    }

    public boolean checkValidity() {
        return validity;
    }
}
