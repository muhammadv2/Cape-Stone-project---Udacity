package com.muhammadv2.going_somewhere.model.network;


import android.net.Uri;

import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.utils.FormattingUtils;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UnsplashApi {

    // String for the trip name to be added as a query
    private String tripName;
    private OkHttpClient client;

    // Both client and tripName are injected to this class by the constructor
    @Inject
    public UnsplashApi(OkHttpClient client, String tripName) {
        this.tripName = tripName;
        this.client = client;
    }

    // Run method that make the network connection and parse the response and return json string
    String run() throws JSONException, IOException {
        Request request = new Request.Builder()
                .url(buildUrl())
                .build();

        Response response = client.newCall(request).execute();
        InputStream inputStream = response.body().byteStream();

        // I'm familiar with retrofit but its only one call with one result that i'm interested in
        // so kept everything simple
        return FormattingUtils.extractUrlFromJson(convertStreamToString(inputStream));

    }

    // Helper method to convert InputStream to string
    private String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader;

        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        StringBuilder sb = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line).append('\n');
        }
        is.close();
        return sb.toString();
    }

    // Build the query uri using the passed trip name and the requested parameters
    private String buildUrl() {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(Constants.UNSPLASH_API_LOCATION)
                .appendPath(Constants.UNSPLASH_ABSOLUTE_1)
                .appendPath(Constants.UNSPLASH_ABSOLUTE_2)
                .appendQueryParameter("client_id", Constants.UNSPLASH_APP_ID)
                .appendQueryParameter("query", tripName)
                .appendQueryParameter("per_page", "1")
                .appendQueryParameter(Constants.UNSPLASH_PHOTO_ORIENTATION, "landscape");

        return builder.build().toString();
    }
}
