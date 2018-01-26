package com.muhammadv2.going_somewhere.model.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.muhammadv2.going_somewhere.utils.UriMatcherUtils;

import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.*;
import static com.muhammadv2.going_somewhere.utils.UriMatcherUtils.CITIES;
import static com.muhammadv2.going_somewhere.utils.UriMatcherUtils.CITY_WITH_ID;
import static com.muhammadv2.going_somewhere.utils.UriMatcherUtils.NOTES;
import static com.muhammadv2.going_somewhere.utils.UriMatcherUtils.NOTE_WITH_ID;
import static com.muhammadv2.going_somewhere.utils.UriMatcherUtils.PLACES;
import static com.muhammadv2.going_somewhere.utils.UriMatcherUtils.PLACE_WITH_ID;
import static com.muhammadv2.going_somewhere.utils.UriMatcherUtils.TRIPS;
import static com.muhammadv2.going_somewhere.utils.UriMatcherUtils.TRIPS_WITH_ID;

public class TravelsProvider extends ContentProvider {

    // Help determine what kind of URI the provider receives and match it to an integer constant
    private static final UriMatcher sUriMatcher = UriMatcherUtils.buildUriMatcher();

    private TravelsDbHelper mTravelsDbHelper;

    @Override
    public boolean onCreate() {

        //instantiate TravelsDbHelper to be able to use the database
        Context context = getContext();
        mTravelsDbHelper = new TravelsDbHelper(context);
        return true;
    }

    //region Insert
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        // Find a matching uri using the helper method of UriMatcher
        int match = sUriMatcher.match(uri);

        Uri returnUri;
        // Switch between the different tables and insert into the one that matches received uri
        switch (match) {
            case TRIPS:
                returnUri = tryToInsert(TripEntry.TABLE_NAME, values, TripEntry.CONTENT_URI);
                break;
            case CITIES:
                returnUri = tryToInsert(CityEntry.TABLE_NAME, values, CityEntry.CONTENT_URI);
                break;
            case PLACES:
                returnUri = tryToInsert(PlaceEntry.TABLE_NAME, values, PlaceEntry.CONTENT_URI);
                break;
            case NOTES:
                returnUri = tryToInsert(NoteEntry.TABLE_NAME, values, NoteEntry.CONTENT_URI);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
        return returnUri;
    }

    /**
     * To help not duplicate code a method working for all insert cases into different tables
     * that accepts
     *
     * @param tableName to determine which table to insert too
     * @param values    which values to insert into that table
     */
    private Uri tryToInsert(String tableName, ContentValues values,
                            Uri contentUri) {

        // Get access to the writable database creating SQLiteDatabase object
        final SQLiteDatabase db = mTravelsDbHelper.getWritableDatabase();

        // Inserting values into the associated table
        long id = db.insert(tableName, null, values);

        if (id > 0) {
            //success insertion
            return ContentUris.withAppendedId(contentUri, id);
        } else {
            throw new SQLException("Failed insert row ");
        }
    }

    //endregion

    //region Query
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Get access to the readable database creating SQLiteDatabase object
        final SQLiteDatabase db = mTravelsDbHelper.getReadableDatabase();

        // Find a matching uri using the helper method of UriMatcher
        int match = sUriMatcher.match(uri);

        Cursor returnCursor;

        // Switch between the different tables and query the one that matches received uri
        switch (match) {
            case TRIPS:
                returnCursor = tryToQuery(db, TripEntry.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            case TRIPS_WITH_ID:
                returnCursor = tryToQuery(db, TripEntry.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            case CITIES:
                returnCursor = tryToQuery(db, CityEntry.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            case CITY_WITH_ID:
                returnCursor = tryToQuery(db, CityEntry.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            case PLACES:
                returnCursor = tryToQuery(db, PlaceEntry.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            case PLACE_WITH_ID:
                returnCursor = tryToQuery(db, PlaceEntry.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            case NOTES:
                returnCursor = tryToQuery(db, NoteEntry.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            case NOTE_WITH_ID:
                returnCursor = tryToQuery(db, NoteEntry.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    private Cursor tryToQuery(SQLiteDatabase db, String tableName, String[] projection,
                              String selection, String[] selectionArgs, String sortOrder) {

        Cursor returnCursor = db.query(tableName,
                projection,
                selection,
                selectionArgs,
                null,
                null, sortOrder);

        return returnCursor;
    }

    //endregion

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
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
}
