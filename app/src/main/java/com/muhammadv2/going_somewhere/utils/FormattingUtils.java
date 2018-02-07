package com.muhammadv2.going_somewhere.utils;

import android.annotation.SuppressLint;

import com.muhammadv2.going_somewhere.model.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FormattingUtils {

    // Take milliseconds and format it as a date and return the formatted date as a string
    private static String milliSecToString(long milliSec) {
        Date date = new Date(milliSec);
        @SuppressLint("SimpleDateFormat") DateFormat formatter =
                new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    // Takes the stored cities String and convert it into ArrayList of cities
    public static ArrayList<City> stringCitiesToArrayList(String cities) {

        String[] strValues = cities.split(",,");
        ArrayList<City> cityList = new ArrayList<>();
        for (int i = 0; i < strValues.length; i++) {
            String cityName = strValues[i];
            cityList.add(new City(cityName, i));
        }
        //Use asList method of Arrays class to convert Java String array to ArrayList
        return cityList;
    }

    // Calculate the duration between the two inserted date and return the complete needed date
    public static String countHowManyDays(long from, long to) {

        long milliseconds = to - from;
        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);

        return "From " + milliSecToString(from) + " Duration " + days + " Days";
    }

    // Extract the json response and return the needed url
    public static String extractUrlFromJson(String response) throws JSONException {

        JSONObject resultObject = new JSONObject(response);
        JSONArray resultsArray = resultObject.getJSONArray("results");
        JSONObject secondObj = resultsArray.getJSONObject(0);
        JSONObject urlsObject = secondObj.getJSONObject("urls");

        return urlsObject.optString("small");

    }

}
