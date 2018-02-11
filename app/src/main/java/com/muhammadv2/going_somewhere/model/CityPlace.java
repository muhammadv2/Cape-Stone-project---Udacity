package com.muhammadv2.going_somewhere.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class CityPlace implements Parcelable {

    String placeName;
    int tripId;

    public CityPlace(String placeName, int tripId) {
        this.placeName = placeName;
        this.tripId = tripId;
    }


    public int getTripId() {
        return tripId;
    }

    public String getPlaceName() {
        return placeName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.placeName);
        dest.writeInt(this.tripId);
    }

    protected CityPlace(Parcel in) {
        this.placeName = in.readString();
        this.tripId = in.readInt();
    }

    public static final Creator<CityPlace> CREATOR = new Creator<CityPlace>() {
        @Override
        public CityPlace createFromParcel(Parcel source) {
            return new CityPlace(source);
        }

        @Override
        public CityPlace[] newArray(int size) {
            return new CityPlace[size];
        }
    };
}
