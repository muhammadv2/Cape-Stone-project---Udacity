package com.muhammadv2.going_somewhere.utils;


import android.content.UriMatcher;

import com.muhammadv2.going_somewhere.model.data.TravelsDbContract;

import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.AUTHORITY;

/**
 * Utility class that take care of defining the different possible URIs and expose
 *
 * @buildUriMatcher method that return the complete UriMatcher
 */
public final class UriMatcherUtils {

    //Defining final integers for the directory of tables and each item
    public static final int TRIPS = 100;
    public static final int TRIPS_WITH_ID = 101;

    public static final int CITIES = 200;
    public static final int CITY_WITH_ID = 201;

    public static final int PLACES = 300;
    public static final int PLACE_WITH_ID = 301;

    public static final int NOTES = 400;
    public static final int NOTE_WITH_ID = 401;

    // Static method that associates URI's with their int match
    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Add matches with addURI *TravelsDbContract added as static import*
        uriMatcher.addURI(AUTHORITY, TravelsDbContract.TripEntry.TABLE_NAME, TRIPS); //trip directory
        uriMatcher.addURI(AUTHORITY, TravelsDbContract.TripEntry.TABLE_NAME + "/#", TRIPS_WITH_ID); //single item

        uriMatcher.addURI(AUTHORITY, TravelsDbContract.CityEntry.TABLE_NAME, CITIES); //city directory
        uriMatcher.addURI(AUTHORITY, TravelsDbContract.CityEntry.TABLE_NAME + "/#", CITY_WITH_ID); //single item

        uriMatcher.addURI(AUTHORITY, TravelsDbContract.PlaceEntry.TABLE_NAME, PLACES); //place directory
        uriMatcher.addURI(AUTHORITY, TravelsDbContract.PlaceEntry.TABLE_NAME + "/#", PLACE_WITH_ID); //single item

        uriMatcher.addURI(AUTHORITY, TravelsDbContract.NoteEntry.TABLE_NAME, NOTES); //note directory
        uriMatcher.addURI(AUTHORITY, TravelsDbContract.NoteEntry.TABLE_NAME + "/#", NOTE_WITH_ID); //single item

        return uriMatcher;
    }
}
