package com.muhammadv2.going_somewhere.ui.tripDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;

public class TripDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        Intent intent = getIntent();
        String tripName = intent.getStringExtra(Constants.ADD_TRIP_NAME);

        setTitle(tripName);  //Trip name
    }
}
