package com.muhammadv2.going_somewhere.model;

import java.util.ArrayList;
import java.util.List;

public final class Trip {

    String tripName;
    long startTime;
    long endTime;
    ArrayList<City> cities;

    public Trip(String tripName, long startTime, long endTime, ArrayList<City> cities) {
        this.tripName = tripName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cities = cities;
    }

    public String getTripName() {
        return tripName;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public List<City> getCities() {
        return cities;
    }
}
