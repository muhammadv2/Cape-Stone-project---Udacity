package com.muhammadv2.going_somewhere.ui.tripDetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.muhammadv2.going_somewhere.R;

public class TripDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
//        Intent intent = getIntent();
//        Bundle bundle = intent.getBundleExtra(Constants.BUNDLE_EXTRA);
//        Trip tripName = bundle.getParcelable(Constants.TRIPS_ARRAY_ID);
//
//        setTitle(tripName.getTripName());  //Trip name
    }
}
