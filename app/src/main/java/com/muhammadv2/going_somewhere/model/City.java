package com.muhammadv2.going_somewhere.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class City implements Parcelable {

    String cityName;
    int cityId;


    public City(String cityName, int cityId) {
        this.cityName = cityName;
        this.cityId = cityId;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    //region Parcel
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityName);
        dest.writeInt(this.cityId);
    }

    protected City(Parcel in) {
        this.cityName = in.readString();
        this.cityId = in.readInt();
    }

    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
        @Override
        public City createFromParcel(Parcel source) {
            return new City(source);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    //endregion
}
