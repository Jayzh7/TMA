package com.jayzh7.tma.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jayzh7.tma.R;
import com.jayzh7.tma.Utils.MyPlacePicker;
import com.jayzh7.tma.Utils.MyTimePicker;
import com.jude.swipbackhelper.SwipeBackHelper;

public class AddEventActivity extends AppCompatActivity {

    public static final int sPLACE_PICKER_REQUEST_CODE_0 = 1;
    public static final int sPLACE_PICKER_REQUEST_CODE_1 = 2;

    private TextView mStartTimeTV;
    private TextView mEndTimeTV;

    private TextView mStartPlaceTV;
    private TextView mEndPlaceTV;
    private Spinner mSpinner;


    private MyTimePicker mStartTimePicker;
    private MyTimePicker mEndTimePicker;
    private MyPlacePicker mStartPlacePicker;
    private MyPlacePicker mEndPlacePicker;
    private LinearLayout mLinearLayout;
    private Activity thisActivity;

    private PopupWindow mPopupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        setContentView(R.layout.activity_add_event);
        thisActivity = this;
        findViews();

        LayoutInflater inflater = (LayoutInflater) thisActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);
        mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mStartTimePicker = new MyTimePicker(thisActivity, mStartTimeTV);
        mEndTimePicker = new MyTimePicker(thisActivity, mEndTimeTV);

        mStartPlacePicker = new MyPlacePicker(thisActivity, mStartPlaceTV, sPLACE_PICKER_REQUEST_CODE_0);
        mEndPlacePicker = new MyPlacePicker(thisActivity, mEndPlaceTV, sPLACE_PICKER_REQUEST_CODE_1);

        initSpinner();
    }

    private void findViews() {
        mStartTimeTV = (TextView) findViewById(R.id.startTimeTV);
        mEndTimeTV = (TextView) findViewById(R.id.endTimeTV);
        mSpinner = (Spinner) findViewById(R.id.eTSpinner);
        mStartPlaceTV = findViewById(R.id.startPlaceTV);
        mEndPlaceTV = findViewById(R.id.endPlaceTV);
        mLinearLayout = findViewById(R.id.linearLayout);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == sPLACE_PICKER_REQUEST_CODE_1 ||
                requestCode == sPLACE_PICKER_REQUEST_CODE_0) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                LatLng latLng = place.getLatLng();
                LatLngBounds latLngBounds = PlacePicker.getLatLngBounds(data);

                if (requestCode == sPLACE_PICKER_REQUEST_CODE_1) {
                    mEndPlacePicker.setBounds(latLngBounds);
                    mEndPlacePicker.setText(place.getName());
                    mEndPlacePicker.setPlaceID(place.getId());
                    mStartPlacePicker.setBounds(latLngBounds);
                } else {
                    mStartPlacePicker.setBounds(latLngBounds);
                    mStartPlacePicker.setText(place.getName());
                    mStartPlacePicker.setPlaceID(place.getId());
                    mEndPlacePicker.setBounds(latLngBounds);
                }
                Log.i("ONRESULT", "Place: " + place.getId());
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("ONRESULT", "Canceled");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add_save) {
            if (mStartPlacePicker.checkValidity() && mEndPlacePicker.checkValidity()) {
                if (mStartTimePicker.checkValidity() && mEndTimePicker.checkValidity()) {
                    // save data to database
                } else {
                    // TODO  point out what is missing
                    // TODO　refactor this segment

                    mPopupWindow.showAtLocation(mLinearLayout, Gravity.CENTER, 0, 0);
                }
            } else {
                mPopupWindow.showAtLocation(mLinearLayout, Gravity.CENTER, 0, 0);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickCancel(View v) {
        mPopupWindow.dismiss();
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


}
