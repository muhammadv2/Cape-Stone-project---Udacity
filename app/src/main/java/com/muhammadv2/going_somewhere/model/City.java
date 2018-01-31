package com.muhammadv2.going_somewhere.model;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public final class City {

    String cityName;
    int tripId;


    @ParcelConstructor
    public City(String cityName, int tripId) {
        this.cityName = cityName;
        this.tripId = tripId;
    }

    public String getCityName() {
        return cityName;
    }

    public int getTripId() {
        return tripId;
    }
}
