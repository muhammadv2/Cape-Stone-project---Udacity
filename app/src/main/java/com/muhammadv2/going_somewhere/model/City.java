package com.muhammadv2.going_somewhere.model;

import org.parceler.Parcel;

@Parcel
class City {

    String cityName;
    int tripId;

    public City(String name, int tripId) {
        this.cityName = name;
        this.tripId = tripId;
    }

    public String getCityName() {
        return cityName;
    }

    public int getTripId() {
        return tripId;
    }
}
