package com.muhammadv2.going_somewhere.model;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

@Parcel
public final class Trip {

    String tripName;
    int startTime;
    int endTime;
    List<City> cities;

    @ParcelConstructor
    public Trip(String tripName, int startTime, int endTime, List<City> cities) {
        this.tripName = tripName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cities = cities;
    }

    public String getTripName() {
        return tripName;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public List<City> getCities() {
        return cities;
    }
}
