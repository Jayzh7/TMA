package com.jayzh7.tma.Utils;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Jay on 10/17/2017.
 */

public class CityPreference {

    SharedPreferences prefs;

    public CityPreference(Activity activity) {
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    // If the user has not chosen a city yet, return
    // Sydney as the default city
    public String getCity() {
        return prefs.getString("city", "Sydney, AU");
    }

    public void setCity(String city) {
        prefs.edit().putString("city", city).commit();
    }

}