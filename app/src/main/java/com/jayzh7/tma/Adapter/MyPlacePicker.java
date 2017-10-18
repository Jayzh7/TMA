package com.jayzh7.tma.Adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;


/**
 * A place picker used to get place input from users
 * @author Jay
 * @version 1.0
 * @since 10/3/2017.
 */

public class MyPlacePicker {
    // 1 represents for end place, 0 represents for start place
    private int mCode;
    private Activity mContext;
    private TextView mTextView;
    private LatLngBounds mBounds;
    private String mPlaceID;
    private String mPlaceName;
    // Check if it's set
    private boolean validity;

    /**
     * public constructor, initialize values
     *
     * @param context  context of parent activity
     * @param textView bind view
     * @param code     0 for start place, 1 for end place
     */
    public MyPlacePicker(Activity context, TextView textView, int code) {
        mContext = context;
        mTextView = textView;
        mCode = code;
        validity = false;
        setListener();
    }

    public void setText(CharSequence string) {
        mTextView.setText(string);
    }

    public void setPlaceName(String placeName) {
        mPlaceName = placeName;
        validity = true;
    }

    public String getPlaceName() {
        return mPlaceName;
    }
    public void testInput() {
        validity = true;
    }

    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }

    public void setPlaceID(String id) {
        mPlaceID = id;
        validity = true;
    }

    public String getPlaceID() {
        return mPlaceID;
    }

    public boolean checkValidity() {
        return validity;
    }

    private void setListener() {
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    if (mBounds != null) intentBuilder.setLatLngBounds(mBounds);
                    mContext.startActivityForResult(intentBuilder.build(mContext), mCode);
                } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
