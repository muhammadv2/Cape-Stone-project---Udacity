package com.muhammadv2.going_somewhere.ui.tripDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;
import com.muhammadv2.going_somewhere.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripDetailsActivity extends AppCompatActivity {

    @BindView(R.id.trip_details_image)
    ImageView imageView;

    @BindView(R.id.appbar_layout)
    AppBarLayout barLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        ButterKnife.bind(this);

        if (getResources().getBoolean(R.bool.rotated)) {
            barLayout.setVisibility(View.GONE);
        } else {
            barLayout.setVisibility(View.VISIBLE);
        }

        Intent intent = getIntent();
        String tripName = intent.getStringExtra(Constants.ADD_TRIP_NAME);
        String imageUrl = intent.getStringExtra(Constants.PLACE_PHOTO);

        ImageUtils.bindImageWithoutBackground(imageUrl, this, imageView);

        setTitle(tripName);  //Trip name
    }
}
