package com.muhammadv2.going_somewhere.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.muhammadv2.going_somewhere.di.ApplicationContext;

import java.util.List;

import javax.inject.Inject;

import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.PlaceEntry;
import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.TripEntry;

// Business logic for the application
public class DataInteractor {

    private ContentResolver resolver;

    @Inject
    public DataInteractor(@ApplicationContext Context context) {
        resolver = context.getContentResolver();
    }


    //region Insert
    public Uri insertIntoTripTable(Trip trip) {

        ContentValues cv = new ContentValues();
        cv.put(TripEntry.COLUMN_TRIP_NAME, trip.getTripName());
        cv.put(TripEntry.COLUMN_TIME_START, trip.getStartTime());
        cv.put(TripEntry.COLUMN_TIME_END, trip.getEndTime());
        cv.put(TripEntry.COLUMN_CITIES_NAMES, extractCitiesNames(trip.getCities()));
        cv.put(TripEntry.COLUMN_IMAGE_URL, trip.getImageUrl());

        return resolver.insert(TripEntry.CONTENT_URI, cv);
    }

    private String extractCitiesNames(List<City> cities) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cities.size(); i++) {
            String cityName = cities.get(i).getCityName();
            sb.append(cityName);
            sb.append(",,");
        }

        return sb.toString();

    }

    public Uri insertIntoPlaceTable(CityPlace place) {

        ContentValues cv = new ContentValues();
        cv.put(PlaceEntry.COLUMN_PLACE_NAME, place.getPlaceName());
        cv.put(PlaceEntry.COLUMN_TRIP_NAME, place.getTripName());
        cv.put(PlaceEntry.COLUMN_CITY_NAME, place.getCityName());

        return resolver.insert(PlaceEntry.CONTENT_URI, cv);
    }


//endregion

    //region Update
    public int updateTripTable(Trip trip, int id) {

        ContentValues cv = new ContentValues();
        cv.put(TripEntry.COLUMN_TRIP_NAME, trip.getTripName());
        cv.put(TripEntry.COLUMN_TIME_START, trip.getStartTime());
        cv.put(TripEntry.COLUMN_TIME_END, trip.getEndTime());
        cv.put(TripEntry.COLUMN_CITIES_NAMES, extractCitiesNames(trip.getCities()));
        cv.put(TripEntry.COLUMN_IMAGE_URL, trip.getImageUrl());

        Uri updateUri = buildAssociatedUri(TripEntry.CONTENT_URI, id);

        return resolver.update(updateUri, cv, null, null);
    }

    public int updatePlaceTable(CityPlace place, int id) {

        ContentValues cv = new ContentValues();
        cv.put(PlaceEntry.COLUMN_PLACE_NAME, place.getPlaceName());
        cv.put(PlaceEntry.COLUMN_TRIP_NAME, place.getTripName());
        cv.put(PlaceEntry.COLUMN_CITY_NAME, place.getCityName());

        Uri updateUri = buildAssociatedUri(PlaceEntry.CONTENT_URI, id);

        return resolver.update(updateUri, cv, null, null);
    }

    //endregion

    //region Delete
    public int deleteFromTripTable(int id) {
        Uri deleteUri = buildAssociatedUri(TripEntry.CONTENT_URI, id);

        return resolver.delete(deleteUri, null, null);
    }

    public int deleteFromPlaceTable(int id) {
        Uri deleteUri = buildAssociatedUri(PlaceEntry.CONTENT_URI, id);

        return resolver.delete(deleteUri, null, null);
    }

    //endregion


    private Uri buildAssociatedUri(Uri uri, int id) {
        return uri.buildUpon().appendPath(String.valueOf(id)).build();
    }


}
