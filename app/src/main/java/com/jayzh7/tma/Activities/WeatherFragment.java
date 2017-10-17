package com.jayzh7.tma.Activities;

import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.jayzh7.tma.Database.DatabaseHelper;
import com.jayzh7.tma.R;
import com.jayzh7.tma.Utils.CityPreference;
import com.jayzh7.tma.Utils.WeatherFetch;

import org.json.JSONObject;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    private TextView mDateTV;
    private Typeface mFont;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDateTV = view.findViewById(R.id.dateTV);
        DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
        if (db.getItemNumberForDate() == 1) {
            int[] date = db.readForDate();
            mDateTV.setText("" + date[1] + "/" + date[2] + "/" + date[0]);
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
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.tff");
        setWeatherForecast(new CityPreference(getActivity()).getCity());
    }

    private void setWeatherForecast(final String city) {
        new Thread() {
            public void run

            {
                JSONObject json = WeatherFetch.getData(getActivity(), city);

                if (json == null) {

                }
            }
        }
    }
}
