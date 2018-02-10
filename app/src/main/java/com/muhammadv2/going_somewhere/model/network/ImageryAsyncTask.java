package com.muhammadv2.going_somewhere.model.network;

import android.os.AsyncTask;

import com.muhammadv2.going_somewhere.di.component.DaggerNetworkComponent;
import com.muhammadv2.going_somewhere.di.component.NetworkComponent;
import com.muhammadv2.going_somewhere.di.module.NetworkModule;

import org.json.JSONException;

import java.io.IOException;


public class ImageryAsyncTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... uris) {

        return initNetworkComponent(uris[0]);

    }

    private String initNetworkComponent(String tripTitle) {
        NetworkComponent component = DaggerNetworkComponent.builder()
                .networkModule(new NetworkModule(tripTitle))
                .build();

        UnsplashApi unsplash = component.unsplashApi();
        try {

            return unsplash.run();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}