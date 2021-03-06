package com.jayzh7.tma.Utils;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;


/**
 * Created by Jay on 10/3/2017.
 */

public class MyPlacePicker {

    private static final String sPREFIX = "origin=place_id:";
    // 1 represents for end place, 0 represents for start place
    private int mCode;
    private Activity mContext;
    private TextView mTextView;
    private LatLngBounds mBounds;
    private String mPlaceID;
    private boolean validity;

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

    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }

    public void setPlaceID(String id) {
        mPlaceID = id;
        validity = true;
    }

    public String getPlaceID() {
        return sPREFIX + mPlaceID;
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
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
