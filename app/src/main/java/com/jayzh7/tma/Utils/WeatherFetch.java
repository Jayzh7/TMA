package com.jayzh7.tma.Utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Fetch data from open weather map api
 * @author Jay
 * @since 10/17/2017.
 * @version 1.0
 */
public class WeatherFetch {
    private static final String sBaseUrl =
            "http://api.openweathermap.org/data/2.5/forecast/daily?";
    private static final String sAppId = "" +
            "&appid=bd5e378503939ddaee76f12ad7a97608";

    private static final String sCod = "cod";

    /**
     * Get data from api
     *
     * @param context context from parent activity
     * @param day     days from now
     * @param lat     latitude of the place
     * @param lon     longitude of the place
     * @return JSONObject that contains the weather info
     */
    public static JSONObject getData(Context context, String day, String lat, String lon) {
        try {
            URL url = new URL(sBaseUrl + "lat=" + lat + "&lon=" + lon + "&cnt=" + day + sAppId);
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String tempString;

            while ((tempString = reader.readLine()) != null) {
                json.append(tempString).append("\n");
            }

            reader.close();

            JSONObject data = new JSONObject(json.toString());

            if (data.getInt(sCod) != 200) {
                return null;
            }
            return data;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
