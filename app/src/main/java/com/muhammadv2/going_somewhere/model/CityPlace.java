package com.muhammadv2.going_somewhere.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class CityPlace implements Parcelable {

    String placeId;
    String placeName;
    int tripId;
    int placeDbId;

    public CityPlace(String placeId, String placeName, int tripId,int placeDbId) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.tripId = tripId;
        this.placeDbId = placeDbId;
    }

    public int getPlaceDbId() {
        return placeDbId;
    }

    public String getPlaceId() {
        return placeId;
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
        dest.writeString(this.placeId);
        dest.writeString(this.placeName);
        dest.writeInt(this.tripId);
        dest.writeInt(this.placeDbId);
    }

    protected CityPlace(Parcel in) {
        this.placeId = in.readString();
        this.placeName = in.readString();
        this.tripId = in.readInt();
        this.placeDbId = in.readInt();
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
