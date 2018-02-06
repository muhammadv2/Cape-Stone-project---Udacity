package com.muhammadv2.going_somewhere.model.network;


import android.net.Uri;
import android.os.AsyncTask;

import com.muhammadv2.going_somewhere.Constants;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class UnsplashApi {

    private String tripName;
    private OkHttpClient client;

    @Inject
    public UnsplashApi(OkHttpClient client, String tripName) {
        this.tripName = tripName;
        this.client = client;
    }

    public String run() {

        String output = null;
        try {
            output = new ImageryAsyncTask().execute(buildUrl()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        if (output != null) {
//            extractUrlFromJson(output);
//        }
        return null;
    }

//    private String extractUrlFromJson(String response) {

//        JSONObject baseJson = null;
//        try {
//            baseJson = new JSONObject(response);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JSONArray resultArray = baseJson.getJSONArray(Constants.RESULT_TAG);
//
//        for (int i = 0; i < resultArray.length(); i++) {
//
//            JSONObject singleMovieObject = resultArray.getJSONObject(i);
//
//            String originalTitle = singleMovieObject.optString(Constants.ORIGINAL_TITLE_TAG);
//        }

    private String buildUrl() {

        // https://api.unsplash.com/photos/?client_id=
        // 90f5cfaef3ee3ea8ac6016ceca58da56d949238ab173fbe858aea6fe6d46848e&

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(Constants.UNSPLASH_API_LOCATION)
                .path(Constants.UNSPLASH_ABSOLUTE_1)
                .path(Constants.UNSPLASH_ABSOLUTE_2)
                .appendQueryParameter("client_id", Constants.UNSPLASH_APP_ID)
                .appendQueryParameter("query", tripName)
                .appendQueryParameter("per_page", "1")
                .appendQueryParameter(Constants.UNSPLASH_PHOTO_ORIENTATION, "landscape");

        Timber.plant(new Timber.DebugTree());
        Timber.d(builder.toString());
        return builder.build().toString();
    }

    private class ImageryAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... uris) {
            Request request = new Request.Builder()
                    .url(uris[0])
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException ex) {
                ex.getCause();
                return null;
            }

        }
    }
}
