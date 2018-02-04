package com.muhammadv2.going_somewhere.ui.tripDetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.muhammadv2.going_somewhere.R;

public class TripDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        setTitle("");  //Trip name
    }
}
