package com.muhammadv2.going_somewhere.model.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import static com.muhammadv2.going_somewhere.model.database.TravelsDbContract.*;

public class TravelsContentProvider extends ContentProvider {

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
        uriMatcher.addURI(AUTHORITY, TripEntry.TABLE_NAME, TRIPS); //trip directory
        uriMatcher.addURI(AUTHORITY, TripEntry.TABLE_NAME + "/#", TRIPS_WITH_ID); //single item

        uriMatcher.addURI(AUTHORITY, CityEntry.TABLE_NAME, CITIES); //city directory
        uriMatcher.addURI(AUTHORITY, CityEntry.TABLE_NAME + "/#", CITY_WITH_ID); //single item

        uriMatcher.addURI(AUTHORITY, PlaceEntry.TABLE_NAME, PLACES); //place directory
        uriMatcher.addURI(AUTHORITY, PlaceEntry.TABLE_NAME + "/#", PLACE_WITH_ID); //single item

        uriMatcher.addURI(AUTHORITY, NoteEntry.TABLE_NAME, NOTES); //note directory
        uriMatcher.addURI(AUTHORITY, NoteEntry.TABLE_NAME + "/#", NOTE_WITH_ID); //single item

        return uriMatcher;
    }


    private TravelsDbHelper mTravelsDbHelper;

    // Help determine what kind of URI the provider receives and match it to an integer constant


    public TravelsContentProvider() {
    }

    @Override
    public boolean onCreate() {

        //instantiate TravelsDbHelper to be able to use the database
        Context context = getContext();
        mTravelsDbHelper = new TravelsDbHelper(context);
        return true;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(@NonNull Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
