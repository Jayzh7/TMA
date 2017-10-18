package com.jayzh7.tma.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SharedPreference, stores the data of the latest chosen start place.
 * @author Jay
 * @since 10/17/2017
 * @version 1.0
 */
public class WeatherPreference {

    SharedPreferences mPrefs;

    public static final String CITY = "city";
    public static final String DEFAULT_CITY = "Sydney, AU";
    public static final String DAY = "day";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    private static final String sDefaultL = "0.00";

    public WeatherPreference(Activity activity) {
        mPrefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    /**
     * This constructor can share data between activities.
     *
     * @param context context
     */
    public WeatherPreference(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    // If the user has not chosen a city yet, return
    // Sydney as the default city
    public String getCity() {
        return mPrefs.getString(CITY, DEFAULT_CITY);
    }

    public String getLongitude() {
        return mPrefs.getString(LONGITUDE, sDefaultL);
    }

    public String getLatitude() {
        return mPrefs.getString(LATITUDE, sDefaultL);
    }

    public int getDay() {
        return mPrefs.getInt(DAY, 0);
    }

    public void setCity(String city) {
        mPrefs.edit().putString(CITY, city).apply();
    }

    public void setLatitude(String latitude) {
        mPrefs.edit().putString(LATITUDE, latitude).apply();
    }

    public void setLongitude(String longitude) {
        mPrefs.edit().putString(LONGITUDE, longitude).apply();
    }

    public void setDay(int day) {
        mPrefs.edit().putInt(DAY, day).apply();
    }

}