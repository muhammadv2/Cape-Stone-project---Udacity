package com.muhammadv2.going_somewhere.model;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

@Parcel
public final class City {

    String cityName;
    int tripId;
    List<Place> places;

    @ParcelConstructor
    public City(String cityName, int tripId, List<Place> places) {
        this.cityName = cityName;
        this.tripId = tripId;
        this.places = places;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public String getCityName() {
        return cityName;
    }

    public int getTripId() {
        return tripId;
    }
}
