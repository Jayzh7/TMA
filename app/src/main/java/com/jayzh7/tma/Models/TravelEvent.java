package com.jayzh7.tma.Models;

import org.joda.time.DateTime;

/**
 * Created by Jay on 10/8/2017.
 */

public class TravelEvent {
    private DateTime mStartTime = null;
    private DateTime mEndTime = null;
    private String mEventName = null;
    private Integer mDuration = -1;
    private String mStartPlaceID;
    private String mEndPlaceID;

    private static final String sPrefix = "origin=place_id:";

    public TravelEvent() {
    }

    public TravelEvent(String name, DateTime start, DateTime end, String startPlaceID, String endPlaceID) {
        mEventName = name;
        mStartTime = start;
        mEndTime = end;
        mStartPlaceID = startPlaceID;
        mEndPlaceID = endPlaceID;
        calcDuration();
    }

    private void calcDuration() {
        mDuration = millisToMinutes(mStartTime.getMillis() - mEndTime.getMillis());
    }

    private int millisToMinutes(long millis) {
        return (int) (millis / 1000 / 60);
    }

    public void setEventName(String name) {
        mEventName = name;
    }

    public void setStartTime(DateTime time) {
        mStartTime = time;
        if (mEndTime != null) calcDuration();
    }

    public void setEndTime(DateTime time) {
        mEndTime = time;
        if (mStartTime != null) calcDuration();
    }

    public void setStartPlaceID(String ID) {
        mStartPlaceID = ID;
    }

    public void setEndPlaceID(String ID) {
        mEndPlaceID = ID;
    }

    public String getStartPlaceID() {
        return mStartPlaceID;
    }

    public String getStartPlaceIDWithPrefix() {
        return sPrefix + mStartPlaceID;
    }

    public String getEndPlaceIDWithPrefix() {
        return sPrefix + mEndPlaceID;
    }

    public String getEndPlaceID() {
        return mEndPlaceID;
    }

    public String getEventName() {
        return mEventName;
    }

    public DateTime getStartTime() {
        return mStartTime;
    }

    public DateTime getEndTime() {
        return mEndTime;
    }

    public Integer getDuration() {
        calcDuration();
        return mDuration;
    }
}
