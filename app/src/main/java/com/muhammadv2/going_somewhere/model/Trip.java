package com.muhammadv2.going_somewhere.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public final class Trip implements Parcelable {

    String tripName;
    long startTime;
    long endTime;
    ArrayList<City> cities;
    String imgUrl;

    public Trip(String tripName, long startTime, long endTime, ArrayList<City> cities, String imgUrl) {
        this.tripName = tripName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cities = cities;
        this.imgUrl = imgUrl;
    }


    public String getImageUrl() {
        return imgUrl;
    }

    public String getTripName() {
        return tripName;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public List<City> getCities() {
        return cities;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tripName);
        dest.writeLong(this.startTime);
        dest.writeLong(this.endTime);
        dest.writeTypedList(this.cities);
        dest.writeString(this.imgUrl);
    }

    protected Trip(Parcel in) {
        this.tripName = in.readString();
        this.startTime = in.readLong();
        this.endTime = in.readLong();
        this.cities = in.createTypedArrayList(City.CREATOR);
        this.imgUrl = in.readString();
    }

    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel source) {
            return new Trip(source);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
}
