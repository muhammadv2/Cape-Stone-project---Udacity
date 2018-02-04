package com.muhammadv2.going_somewhere.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import java.util.List;

import javax.inject.Inject;

import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.NoteEntry;
import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.PlaceEntry;
import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.TripEntry;

// Business logic for the application
public class DataInteractor {

    private Context context;
    private ContentResolver resolver;

    @Inject
    public DataInteractor(Context context) {
        this.context = context;
        resolver = context.getContentResolver();
    }

    //region Insert
    public Uri insertIntoTripTable(Trip trip) {

        ContentValues cv = new ContentValues();
        cv.put(TripEntry.COLUMN_TRIP_NAME, trip.getTripName());
        cv.put(TripEntry.COLUMN_TIME_START, trip.getStartTime());
        cv.put(TripEntry.COLUMN_TIME_END, trip.getEndTime());
        cv.put(TripEntry.COLUMN_CITIES_NAMES, extractCitiesNames(trip.getCities()));

        return resolver.insert(TripEntry.CONTENT_URI, cv);
    }

    private String extractCitiesNames(List<City> cities) {

        StringBuilder sb = new StringBuilder();
        for (City city : cities) {
            sb.append(city.getCityName());
            sb.append(",,");
        }

        return sb.toString();

    }

    public Uri insertIntoPlaceTable(Place place) {

        ContentValues cv = new ContentValues();
        cv.put(PlaceEntry.COLUMN_PLACE_NAME, place.getPlaceName());
        cv.put(PlaceEntry.COLUMN_TIME_START, place.getTimeStart());
        cv.put(PlaceEntry.COLUMN_TIME_END, place.getTimeEnd());
        cv.put(PlaceEntry.COLUMN_TRIP_ID, place.getTripId());
        cv.put(PlaceEntry.COLUMN_CITY_ID, place.getCityId());

        return resolver.insert(PlaceEntry.CONTENT_URI, cv);
    }

    public Uri insertIntoNoteTable(Note note) {

        ContentValues cv = new ContentValues();
        cv.put(NoteEntry.COLUMN_NOTE_TITLE, note.getNoteTitle());
        cv.put(NoteEntry.COLUMN_NOTE_BODY, note.getNoteBody());
        cv.put(NoteEntry.COLUMN_IS_TOGGLE_NOTE, note.isToggleNote());


        return resolver.insert(NoteEntry.CONTENT_URI, cv);
    }
//endregion

    //region Update
    public int updateTripTable(Trip trip) {

        ContentValues cv = new ContentValues();
        cv.put(TripEntry.COLUMN_TRIP_NAME, trip.getTripName());
        cv.put(TripEntry.COLUMN_TIME_START, trip.getStartTime());
        cv.put(TripEntry.COLUMN_TIME_END, trip.getEndTime());
        cv.put(TripEntry.COLUMN_CITIES_NAMES, extractCitiesNames(trip.getCities()));

        return resolver.update(TripEntry.CONTENT_URI, cv, null, null);
    }

    public int updatePlaceTable(Place place) {

        ContentValues cv = new ContentValues();
        cv.put(PlaceEntry.COLUMN_PLACE_NAME, place.getPlaceName());
        cv.put(PlaceEntry.COLUMN_TIME_START, place.getTimeStart());
        cv.put(PlaceEntry.COLUMN_TIME_END, place.getTimeEnd());
        cv.put(PlaceEntry.COLUMN_TRIP_ID, place.getTripId());
        cv.put(PlaceEntry.COLUMN_CITY_ID, place.getCityId());

        return resolver.update(PlaceEntry.CONTENT_URI, cv, null, null);
    }

    public int updateNoteTable(Note note) {

        ContentValues cv = new ContentValues();
        cv.put(NoteEntry.COLUMN_NOTE_TITLE, note.getNoteTitle());
        cv.put(NoteEntry.COLUMN_NOTE_BODY, note.getNoteBody());
        cv.put(NoteEntry.COLUMN_IS_TOGGLE_NOTE, note.isToggleNote());

        return resolver.update(NoteEntry.CONTENT_URI, cv, null, null);

    }

}
