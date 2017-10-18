package com.jayzh7.tma.Models;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Data model of travel event
 * @author Jay
 * @version 1.0
 * @since 10/8/2017.
 */
public class TravelEvent implements Serializable {
    private DateTime mStartTime; // Start time
    private int mStartT;         // Start time in minutes
    private int mEndT;           // End time in minutes
    private DateTime mEndTime;   // End time
    private String mEventName;   // Event name
    private Integer mDuration = -1; // Duation of the event
    private String mStartPlaceID;   // ID of the start place
    private String mEndPlaceID;     // ID of the end place
    private EventType mEventType;   // Event type
    private String mFromPlace;      // Name of Start place
    private String mEndPlace;       // Name of End place

    private static final String sPrefix = "origin=place_id:";

    // Empty public constructor
    public TravelEvent() {
    }

    /**
     * Public constructor
     *
     * @param name         name of the event
     * @param start        start time
     * @param end          end time
     * @param startPlaceID ID of the start place
     * @param endPlaceID   ID of the end place
     */
    public TravelEvent(String name, DateTime start, DateTime end, String startPlaceID, String endPlaceID) {
        mEventName = name;
        mStartTime = start;
        mEndTime = end;
        mStartPlaceID = startPlaceID;
        mEndPlaceID = endPlaceID;
        calcDuration();
    }

    public TravelEvent(String name, DateTime start, DateTime end, String startPlaceId, String endPlaceId, String startPlace, String endPlace, EventType eventType) {
        mEventName = name;
        mStartTime = start;
        mEndTime = end;
        mStartPlaceID = startPlaceId;
        mEndPlaceID = endPlaceId;
        mFromPlace = startPlace;
        mEndPlace = endPlace;
        mEventType = eventType;
        calcDuration();
    }

    public TravelEvent(String name, String startPlace, String endPlace, int startTime, int endTime, int eventType) {
        mEventName = name;
        mStartT = startTime;
        mEndT = endTime;
        mFromPlace = startPlace;
        mEndPlace = endPlace;
        mEventType = EventType.values()[eventType];
    }

    /**
     * Calculate duration of the event
     */
    private void calcDuration() {
        mDuration = millisToMinutes(mStartTime.getMillis() - mEndTime.getMillis());
    }

    /**
     * Convert milliseconds to minutes
     * @param millis milliseconds
     * @return minutes
     */
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

    public String getFromPlace() {
        return mFromPlace;
    }

    public String getToPlace() {
        return mEndPlace;
    }

    public int getStartT() {
        return mStartT;
    }

    public int getEndT() {
        return mEndT;
    }

    public EventType getEventType() {
        return mEventType;
    }

    public Integer getDuration() {
        calcDuration();
        return mDuration;
    }
}
