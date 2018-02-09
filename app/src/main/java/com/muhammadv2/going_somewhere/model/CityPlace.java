package com.muhammadv2.going_somewhere.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class CityPlace implements Parcelable {

    String placeName;
    String cityName;
    String tripName;

    public CityPlace(String placeName, String cityName, String tripName) {
        this.placeName = placeName;
        this.cityName = cityName;
        this.tripName = tripName;
    }


    public String getTripName() {
        return tripName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getCityName() {
        return cityName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.placeName);
        dest.writeString(this.cityName);
        dest.writeString(this.tripName);
    }

    protected CityPlace(Parcel in) {
        this.placeName = in.readString();
        this.cityName = in.readString();
        this.tripName = in.readString();
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
