package com.muhammadv2.going_somewhere.utils;

import android.annotation.SuppressLint;

import com.muhammadv2.going_somewhere.model.City;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FormattingUtils {

    public static String milliSecToString(long milliSec) {
        Date date = new Date(milliSec);
        @SuppressLint("SimpleDateFormat") DateFormat formatter =
                new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

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

    public static String countHowManyDays(long from, long to) {

        long milliseconds = to - from;
        int days = (int) ((milliseconds / (1000 * 60 * 60 * 24)) % 7);

        return "From " + milliSecToString(from) + " Duration " + days + " Days";
    }
}
