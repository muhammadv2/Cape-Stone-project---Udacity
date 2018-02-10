package com.muhammadv2.going_somewhere.model.network;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;

import javax.inject.Inject;

public class PlacesApi {

    private Context context;
    private Activity activity;

    @Inject
    public PlacesApi(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    void instantiate() {
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(context, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(context, null);

        // TODO: Start using the Places API.

    }

    public void startPlacePicker() {


    }
}
