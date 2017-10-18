package com.jayzh7.tma.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jayzh7.tma.Adapter.MyPlacePicker;
import com.jayzh7.tma.Adapter.MyTimePicker;
import com.jayzh7.tma.Adapter.TravelEventAdapter;
import com.jayzh7.tma.Database.DatabaseHelper;
import com.jayzh7.tma.Models.EventType;
import com.jayzh7.tma.Models.TravelEvent;
import com.jayzh7.tma.R;
import com.jayzh7.tma.Utils.DateTimeConverter;
import com.jayzh7.tma.Utils.WeatherPreference;
import com.jude.swipbackhelper.SwipeBackHelper;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Activity for adding new events or updating existing events.
 * @author Jay
 * @version 1.0
 * @since 10/17/2017
 */
public class AddEventActivity extends AppCompatActivity {

    // Represents start place picker
    public static final int PLACE_PICKER_REQUEST_CODE_0 = 1;
    // Represents end place picker
    public static final int PLACE_PICKER_REQUEST_CODE_1 = 2;
    public static final int MINUTES_IN_A_DAY = 1440;

    // Warning messages for different warnings.
    private static final String sMissingInfo = "Please fill out all the blanks";
    private static final String sTimeConflict = "The time period that you've chosen is not available";
    private static final String sInvalidTime = "Please input valid start time and end time";

    private EditText mEventNameET;

    private TextView mStartTimeTV;
    private TextView mEndTimeTV;

    private TextView mStartPlaceTV;
    private TextView mEndPlaceTV;

    private Spinner mSpinner;
    private EventType mType;

    private MyTimePicker mStartTimePicker;
    private MyTimePicker mEndTimePicker;
    private MyPlacePicker mStartPlacePicker;
    private MyPlacePicker mEndPlacePicker;

    private LinearLayout mLinearLayout;
    private Activity thisActivity;

    private DatabaseHelper mDB;
    private PopupWindow mPopupWindow;
    private TextView mPopupText;

    // These variables are used to identify a edit event or a new event
    private boolean mNewEvent;
    private int mId;

    /**
     * Called when the activity is starting. Does most of the initialization.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in
     *                           onSaveInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        setContentView(R.layout.activity_add_event);
        thisActivity = this;
        findViews();


        mDB = DatabaseHelper.getInstance(this);
        mType = EventType.SIGHTSEEING;
        LayoutInflater inflater = (LayoutInflater) thisActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);
        mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupText = popupView.findViewById(R.id.popupTV);

        mStartTimePicker = new MyTimePicker(thisActivity, mStartTimeTV);
        mEndTimePicker = new MyTimePicker(thisActivity, mEndTimeTV);

        mStartPlacePicker = new MyPlacePicker(thisActivity, mStartPlaceTV, PLACE_PICKER_REQUEST_CODE_0);
        mEndPlacePicker = new MyPlacePicker(thisActivity, mEndPlaceTV, PLACE_PICKER_REQUEST_CODE_1);

        initSpinner();

        Intent intent = getIntent();
        mId = intent.getIntExtra(TravelEventAdapter.START_TIME, 0);
        if (mId != 0) {
            setInput(intent.getSerializableExtra(TravelEventAdapter.TRAVEL_EVENT));
            mNewEvent = false;
        } else {
            mNewEvent = true;
            testInput();
        }

    }

    /**
     * Set input for updating event.
     *
     * @param serializableExtra Object received from intent.
     */
    private void setInput(Serializable serializableExtra) {
        TravelEvent event = (TravelEvent) serializableExtra;

        mEventNameET.setText(event.getEventName());

        mSpinner.setSelection(event.getEventType().ordinal());

        mStartTimeTV.setText(new DateTimeConverter(event.getStartT()).getConvertedTime());
        mEndTimeTV.setText(new DateTimeConverter(event.getEndT()).getConvertedTime());

        mStartTimePicker.setTime(event.getStartT());
        mEndTimePicker.setTime(event.getEndT());

        mStartPlacePicker.setText(event.getFromPlace());
        mEndPlacePicker.setText(event.getToPlace());
        mStartPlacePicker.setPlaceName(event.getFromPlace());
        mEndPlacePicker.setPlaceName(event.getToPlace());
    }


    /**
     * Input test info
     */
    private void testInput() {
        mStartTimePicker.testInput(10, 20);
        mEndTimePicker.testInput(10, 40);

        mStartPlacePicker.testInput();
        mStartPlacePicker.testInput();
    }

    /**
     * Bind views
     */
    private void findViews() {
        mStartTimeTV = findViewById(R.id.startTimeTV);
        mEndTimeTV = findViewById(R.id.endTimeTV);
        mSpinner = findViewById(R.id.eTSpinner);
        mStartPlaceTV = findViewById(R.id.startPlaceTV);
        mEndPlaceTV = findViewById(R.id.endPlaceTV);
        mLinearLayout = findViewById(R.id.linearLayout);
        mEventNameET = findViewById(R.id.eventNameET);
    }

    /**
     * Initialize spinner to string array
     */
    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.event_type, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }

    /**
     * Process result from place pickers
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                    used to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST_CODE_1 ||
                requestCode == PLACE_PICKER_REQUEST_CODE_0) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                LatLngBounds latLngBounds = PlacePicker.getLatLngBounds(data);

                if (requestCode == PLACE_PICKER_REQUEST_CODE_1) {
                    // Result from EndPlacePicker
                    mEndPlacePicker.setBounds(latLngBounds);
                    mEndPlacePicker.setText(place.getName());
                    mEndPlacePicker.setPlaceID(place.getId());
                    mEndPlacePicker.setPlaceName(place.getName().toString());
                    mStartPlacePicker.setBounds(latLngBounds);
                } else {
                    // Result from StartPlacePicker
                    mStartPlacePicker.setBounds(latLngBounds);
                    mStartPlacePicker.setText(place.getName());
                    mStartPlacePicker.setPlaceID(place.getId());
                    mStartPlacePicker.setPlaceName(place.getName().toString());
                    mEndPlacePicker.setBounds(latLngBounds);

                    new WeatherPreference(getApplicationContext()).setLongitude(String.valueOf(place.getLatLng().longitude));
                    new WeatherPreference(getApplicationContext()).setLatitude(String.valueOf(place.getLatLng().latitude));
                }
            } else if (resultCode == RESULT_CANCELED) {
                // Nothing to do
            }
        }
    }

    /**
     * Inflate options menu
     * @param menu the menu where option menu will be inflated
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    /**
     * This is called whenever an item in options menu is selected.
     * @param item The menu item that was selected
     * @return super
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add_save) {
            // Save data
            if (!mNewEvent) {
                // It is updating an event so delete old event and clear time line
                mDB.deleteEvent(mId);
            }

            // Check if all blanks are filled
            if (mStartPlacePicker.checkValidity() && mEndPlacePicker.checkValidity()
                    && mStartTimePicker.checkValidity() && mEndTimePicker.checkValidity()) {
                // Check if input time make sense
                if (getMinOfDateTime(mStartTimePicker.getDateTime()) < getMinOfDateTime(mEndTimePicker.getDateTime())) {
                    // Check if time period is available
                    if (checkTimeValidity()) {
                        // save data to database
                        // Add a new event to database
                        mDB.insertTravelEvent(
                                new TravelEvent(
                                        mEventNameET.getText().toString(),
                                        mStartTimePicker.getDateTime(),
                                        mEndTimePicker.getDateTime(),
                                        mStartPlacePicker.getPlaceID(),
                                        mEndPlacePicker.getPlaceID(),
                                        mEndPlacePicker.getPlaceName(),
                                        mStartPlacePicker.getPlaceName(),
                                        EventType.valueOf(mSpinner.getSelectedItem().toString())
                                )
                        );

                        this.finish();
                        return true;
                    } else {
                        mPopupText.setText(sTimeConflict);
                    }
                } else {
                    mPopupText.setText(sInvalidTime);
                }
            } else {
                mPopupText.setText(sMissingInfo);
            }

            mPopupWindow.showAtLocation(mLinearLayout, Gravity.CENTER, 0, 0);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Dismiss the popup window
     * @param v
     */
    public void onClickCancel(View v) {
        mPopupWindow.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    /**
     * Check if the time period users have chosen is available
     *
     * @return true available
     */
    private boolean checkTimeValidity() {
        // Retrieve data from database
        int[] array = mDB.readForTimeLine();

        DateTime start = mStartTimePicker.getDateTime();
        DateTime end = mEndTimePicker.getDateTime();
        Boolean available = true;
        for (int i = getMinOfDateTime(start); i < getMinOfDateTime(end); i++) {
            if (array[i] != 1) {
                available = false;
                break;
            }
        }
        return available;
    }

    /**
     * Get minutes from a DateTime object
     * @param dateTime to be converted
     * @return minutes
     */
    private int getMinOfDateTime(DateTime dateTime) {
        return dateTime.getHourOfDay() * 60 + dateTime.getMinuteOfHour();
    }
}
