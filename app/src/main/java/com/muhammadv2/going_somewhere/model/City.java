package com.muhammadv2.going_somewhere.model;

import org.parceler.Parcel;

import java.util.List;

@Parcel
final class City {

    String cityName;
    int tripId;
    List<Place> places;

    public List<Place> getPlaces() {
        return places;
    }

    public City(String name, int tripId, List<Place> places) {
        this.cityName = name;
        this.tripId = tripId;
        this.places = places;
    }

    public String getCityName() {
        return cityName;
    }

    public int getTripId() {
        return tripId;
    }
}
