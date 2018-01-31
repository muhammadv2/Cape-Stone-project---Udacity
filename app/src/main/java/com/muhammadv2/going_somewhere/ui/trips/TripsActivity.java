package com.muhammadv2.going_somewhere.ui.trips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.muhammadv2.going_somewhere.R;
import com.muhammadv2.going_somewhere.di.component.ActivityComponent;
import com.muhammadv2.going_somewhere.di.component.DaggerActivityComponent;
import com.muhammadv2.going_somewhere.di.module.ActivityModule;

public class TripsActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getActivityComponent().inject(this);

        activity = this;


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .build();
        }
        return activityComponent;
    }



}
