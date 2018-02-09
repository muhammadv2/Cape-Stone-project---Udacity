package com.muhammadv2.going_somewhere.utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.model.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FormattingUtils {

    // Take milliseconds and format it as a date and return the formatted date as a string
    public static String milliSecToString(long milliSec) {
        Date date = new Date(milliSec);
        @SuppressLint("SimpleDateFormat") DateFormat formatter =
                new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public static void formatTheSelectedDate(Intent data, EditText etAddDateFrom, EditText etAddDateTo) {
        Bundle bundle = data.getExtras();
        int day = bundle.getInt(Constants.DAY_PICKER);
        int month = bundle.getInt(Constants.MONTH_PICKER);
        int year = bundle.getInt(Constants.YEAR_PICKER);
        boolean bool = bundle.getBoolean(Constants.BOOL_PICKER);

        String strDate = String.valueOf(day + "/" + month + "/" + year);

        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        String dateNeeded = null;
        try {
            date = df.parse(strDate);
            dateNeeded = df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (bool) {
            etAddDateFrom.setText(dateNeeded);
        } else {
            etAddDateTo.setText(dateNeeded);
        }
    }

    public static long parseDateToMiSeconds(String stringDate) {

        if (stringDate == null) {
            return 0;
        }

        @SuppressLint("SimpleDateFormat") java.text.DateFormat formatter =
                new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = formatter.parse(stringDate);
            return date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Takes the stored cities String and convert it into ArrayList of cities
    public static ArrayList<City> stringCitiesToArrayList(String cities) {

        String[] strValues = cities.split(",,");
        ArrayList<City> cityList = new ArrayList<>();
        for (int i = 0; i < strValues.length; i++) {
            cityList.add(new City(strValues[i], i));
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
