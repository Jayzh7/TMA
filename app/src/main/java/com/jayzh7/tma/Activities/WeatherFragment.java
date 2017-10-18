package com.jayzh7.tma.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.jayzh7.tma.Database.DatabaseHelper;
import com.jayzh7.tma.R;
import com.jayzh7.tma.Utils.WeatherFetch;
import com.jayzh7.tma.Utils.WeatherPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


/**
 * Fragment that display weather info
 * @author Jay
 * @version 1.0
 * @since 10/14/2017
 */
public class WeatherFragment extends Fragment {

    private TextView mDateTV;
    private TextView mWarning;
    private TextView mCityTV;
    private TextView mWindTV;
    private TextView mTempTV;
    private TextView mSummary;

    private WeatherPreference mPreference;

    private Handler mHandler;

    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Inflate views and notify activity that option menu will be set
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container          If non-null, this is the parent view that the fragment's UI should be
     *                           attached to. The fragment should not add the view itself, but this can be
     *                           used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here.
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    /**
     * Bind views and listeners, update weather info
     * @param view The View returned by onCreateView
     * @param savedInstanceState not in use
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final WeatherPreference preference = new WeatherPreference(getActivity().getApplicationContext());

        mHandler = new Handler();
        mDateTV = view.findViewById(R.id.dateTV);
        mWarning = view.findViewById(R.id.warning);
        mCityTV = view.findViewById(R.id.cityTV);
        mWindTV = view.findViewById(R.id.windTV);
        mTempTV = view.findViewById(R.id.tempTV);
        mSummary = view.findViewById(R.id.summaryTV);

        DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
        if (db.getItemNumberForDate() == 1) {
            int[] date = db.readForDate();
            mDateTV.setText("" + date[1] + "/" + date[2] + "/" + date[0]);
            Calendar calendar = Calendar.getInstance();
            long diff = calcDif(calendar, date[2], date[1], date[0]);
            if (diff < 14) {
                new WeatherPreference(getActivity().getApplicationContext()).setDay((int) diff);
                mWarning.setTextColor(Color.GRAY);
            } else {
                mWarning.setTextColor(Color.RED);
            }
        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
                if (db.getItemNumberForDate() == 0) {
                    db.insertDate(year, month++, day);
                } else {
                    db.updateDate(year, month++, day);
                }
                mDateTV.setText("" + day + "/" + month + "/" + year);

                Calendar calendar = Calendar.getInstance();
                long diff = calcDif(calendar, day, month, year);
                if (diff < 14) {
                    new WeatherPreference(getActivity().getApplicationContext()).setDay((int) diff);
                    upDateWeatherData(new WeatherPreference(getActivity().getApplicationContext()));
                    mWarning.setTextColor(Color.GRAY);
                } else {
                    mWarning.setTextColor(Color.RED);
                }
            }
        };

        mDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(getActivity(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        upDateWeatherData(preference);
    }

    /**
     * Calculate how many days is now to the selected date.
     *
     * @param calendar selected date
     * @param day      current day
     * @param month    current month
     * @param year     current year
     * @return difference
     */
    private long calcDif(Calendar calendar, int day, int month, int year) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(year, month - 1, day);

        if (calendar1.get(Calendar.MINUTE) > calendar.get(Calendar.MINUTE)) {
            return 8;
        } else {
            return (calendar1.getTimeInMillis() - calendar.getTimeInMillis()) / (1440 * 60 * 1000);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Update weather with given preference
     *
     * @param preferences Sharedpreference,for storing location and days
     */
    private void upDateWeatherData(WeatherPreference preferences) {
        if (preferences.getDay() == 0)
            return;

        String lat = preferences.getLatitude();
        String lon = preferences.getLongitude();
        int day = preferences.getDay();

        new FetchWeather().execute(String.valueOf(day), lat, lon);
    }

    /**
     * Async task that does fetch weather info in the background while showing a progress dialog
     */
    class FetchWeather extends AsyncTask<String, Void, JSONObject> {

        private static final String sMessage = "Fetching weather ..";
        public static final String CITY = "city";
        public static final String NAME = "name";
        public static final String WEATHER = "weather";
        public static final String LIST = "list";
        public static final String MIN = "min";
        public static final String MAX = "max";
        public static final String TEMP = "temp";
        public static final String DESCRIPTION = "description";
        public static final String SPEED = "speed";

        private ProgressDialog mProgressDialog;

        private int mDay;

        /**
         * Prepare the progress dialog and show it.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage(sMessage);
            mProgressDialog.show();
        }

        /**
         * Fetch weather data
         *
         * @param params Geographic info and an int that indicates get weather of how many days later
         * @return JSONObject that contains weather info
         */
        @Override
        protected JSONObject doInBackground(String... params) {
            mDay = Integer.parseInt(params[0]);
            if (mDay < 0) {
                mDay = 1;
                mWarning.setTextColor(Color.RED);
            }

            JSONObject object = WeatherFetch.getData(getActivity(), String.valueOf(mDay + 1), params[1], params[2]);
            return object;
        }

        /**
         * Set the text views with weather info and dismiss the progress dialog
         *
         * @param json
         */
        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            if (json != null) {
                try {
                    JSONObject location = json.getJSONObject(CITY);
                    String city = location.getString(NAME);
                    if (city.equals("")) {
                        mCityTV.setText(getActivity().getString(R.string.unknown_city));
                    } else {
                        mCityTV.setText(city);
                    }

                    JSONObject list = json.getJSONArray(LIST).getJSONObject(mDay);
                    JSONObject temp = list.getJSONObject(TEMP);
                    JSONObject we = list.getJSONArray(WEATHER).getJSONObject(0);

                    mTempTV.setText(temp.getString(MAX) + " ~ " + temp.get(MIN));
                    mSummary.setText(we.getString(DESCRIPTION));
                    mWindTV.setText(list.getString(SPEED));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mProgressDialog.dismiss();
        }
    }
}
