package com.muhammadv2.going_somewhere.model;

import org.parceler.Parcel;

@Parcel
final class Place {

    String placeName;
    int cityId;
    int timeStart;
    int timeEnd;

    public Place(String name, int cityId, int timeStart, int timeEnd) {
        this.placeName = name;
        this.cityId = cityId;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
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
