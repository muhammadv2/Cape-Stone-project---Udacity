package com.muhammadv2.going_somewhere.ui.trips.addTrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;
import com.muhammadv2.going_somewhere.model.Trip;

/**
 * Activity to work as a addCityContainer for AddTripFragment it's layout contain only Fragment root
 */
public class AddTripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        //set new title for this activity

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new pet or editing an existing one.
        Intent intent = getIntent();
        Trip trip = intent.getParcelableExtra(Constants.TRIPS_ARRAY_ID);
        if (trip == null) {
            setTitle(Constants.ADD_TRIP_NAME);
        } else {
            setTitle("Edit Trip " + trip.getTripName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dialog, menu);
        return true;
    }
}
