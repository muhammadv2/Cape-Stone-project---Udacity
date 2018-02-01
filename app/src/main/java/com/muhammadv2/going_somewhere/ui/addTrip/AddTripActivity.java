package com.muhammadv2.going_somewhere.ui.addTrip;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;

/**
 * Activity to work as a addCityContainer for AddTripFragment it's layout contain only Fragment root
 */
public class AddTripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        //set new title for this activity
        setTitle(Constants.ADD_TRIP_NAME);
    }
}
