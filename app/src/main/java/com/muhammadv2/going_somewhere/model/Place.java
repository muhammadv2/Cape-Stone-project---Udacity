package com.muhammadv2.going_somewhere.model;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public final class Place {

    String placeName;
    int cityId;
    int timeStart;
    int timeEnd;

    @ParcelConstructor
    public Place(String placeName, int cityId, int timeStart, int timeEnd) {
        this.placeName = placeName;
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
