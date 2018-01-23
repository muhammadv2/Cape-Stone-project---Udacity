package com.muhammadv2.going_somewhere.model;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public final class Trip {

     String name;
     int startTime;
     int endTime;
     List<City> cities;

    public Trip(String name, int startTime, int endTime, List<City> cities) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cities = cities;
    }

    public String getName() {
        return name;
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
