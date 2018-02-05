package com.muhammadv2.going_somewhere.model;

public final class Place {

    String placeName;
    String placeId;
    int cityId;
    int tripId;
    int timeStart;
    int timeEnd;

    public Place(String placeName, String placeId, int cityId, int tripId, int timeStart, int timeEnd) {
        this.placeName = placeName;
        this.placeId = placeId;
        this.cityId = cityId;
        this.tripId = tripId;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public int getTripId() {
        return tripId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public int getCityId() {
        return cityId;
    }

    public int getTimeStart() {
        return timeStart;
    }

    public int getTimeEnd() {
        return timeEnd;
    }
}
