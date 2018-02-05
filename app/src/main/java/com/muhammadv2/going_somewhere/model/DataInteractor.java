package com.muhammadv2.going_somewhere.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.muhammadv2.going_somewhere.di.ApplicationContext;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.NoteEntry;
import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.PlaceEntry;
import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.TripEntry;

// Business logic for the application
public class DataInteractor {

    private ContentResolver resolver;

    @Inject
    public DataInteractor(@ApplicationContext Context context) {
        resolver = context.getContentResolver();
    }

    //Todo (7) Implement the query helper methods too :)

    //region Insert
    public Uri insertIntoTripTable(Trip trip) {

        ContentValues cv = new ContentValues();
        cv.put(TripEntry.COLUMN_TRIP_NAME, trip.getTripName());
        cv.put(TripEntry.COLUMN_TIME_START, trip.getStartTime());
        cv.put(TripEntry.COLUMN_TIME_END, trip.getEndTime());
        cv.put(TripEntry.COLUMN_CITIES_NAMES, extractCitiesNames(trip.getCities()));

        Timber.plant(new Timber.DebugTree());
        Timber.d("our Trip values " + trip.getTripName() + " " + trip.startTime + " " + extractCitiesNames(trip.cities));

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
    public int updateTripTable(Trip trip, int id) {

        ContentValues cv = new ContentValues();
        cv.put(TripEntry.COLUMN_TRIP_NAME, trip.getTripName());
        cv.put(TripEntry.COLUMN_TIME_START, trip.getStartTime());
        cv.put(TripEntry.COLUMN_TIME_END, trip.getEndTime());
        cv.put(TripEntry.COLUMN_CITIES_NAMES, extractCitiesNames(trip.getCities()));

        Uri updateUri = buildAssociatedUri(TripEntry.CONTENT_URI, id);

        return resolver.update(updateUri, cv, null, null);
    }

    public int updatePlaceTable(Place place, int id) {

        ContentValues cv = new ContentValues();
        cv.put(PlaceEntry.COLUMN_PLACE_NAME, place.getPlaceName());
        cv.put(PlaceEntry.COLUMN_TIME_START, place.getTimeStart());
        cv.put(PlaceEntry.COLUMN_TIME_END, place.getTimeEnd());
        cv.put(PlaceEntry.COLUMN_TRIP_ID, place.getTripId());
        cv.put(PlaceEntry.COLUMN_CITY_ID, place.getCityId());

        Uri updateUri = buildAssociatedUri(PlaceEntry.CONTENT_URI, id);

        return resolver.update(updateUri, cv, null, null);
    }

    public int updateNoteTable(Note note, int id) {

        ContentValues cv = new ContentValues();
        cv.put(NoteEntry.COLUMN_NOTE_TITLE, note.getNoteTitle());
        cv.put(NoteEntry.COLUMN_NOTE_BODY, note.getNoteBody());
        cv.put(NoteEntry.COLUMN_IS_TOGGLE_NOTE, note.isToggleNote());

        Uri updateUri = buildAssociatedUri(NoteEntry.CONTENT_URI, id);

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

    public int deleteFromNoteTable(int id) {
        Uri deleteUri = buildAssociatedUri(NoteEntry.CONTENT_URI, id);

        return resolver.delete(deleteUri, null, null);
    }

    //endregion


    private Uri buildAssociatedUri(Uri uri, int id) {
        return uri.buildUpon().appendPath(String.valueOf(id)).build();
    }


}
