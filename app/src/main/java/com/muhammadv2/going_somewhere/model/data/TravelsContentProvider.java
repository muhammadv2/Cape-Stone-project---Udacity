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
import static com.muhammadv2.going_somewhere.utils.UriMatcherUtils.NOTES;
import static com.muhammadv2.going_somewhere.utils.UriMatcherUtils.PLACES;
import static com.muhammadv2.going_somewhere.utils.UriMatcherUtils.TRIPS;

public class TravelsContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = UriMatcherUtils.buildUriMatcher();

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

    //region Insert
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {


        // Find a matching uri using the helper method of UriMatcher
        int match = sUriMatcher.match(uri);

        Uri returnUri;
        // Switch between the different tables and insert into the one that matches our uri
        switch (match) {
            case TRIPS:
                returnUri = tryToInsertIntoDifferentTables(TripEntry.TABLE_NAME, values);
                break;

            case CITIES:
                returnUri = tryToInsertIntoDifferentTables(CityEntry.TABLE_NAME, values);
                break;
            case PLACES:
                returnUri = tryToInsertIntoDifferentTables(PlaceEntry.TABLE_NAME, values);
                break;
            case NOTES:
                returnUri = tryToInsertIntoDifferentTables(NoteEntry.TABLE_NAME, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        return returnUri;

    }

    private Uri tryToInsertIntoDifferentTables(String tableName, ContentValues values) {

        // Get access to the writable database creating SQLiteDatabase object
        final SQLiteDatabase db = mTravelsDbHelper.getWritableDatabase();

        // Inserting values into Trips table
        long id = db.insert(tableName, null, values);

        if (id > 0) {
            //success insertion
            return ContentUris.withAppendedId(TripEntry.CONTENT_URI, id);
        } else {
            throw new SQLException("Failed insert row ");
        }
    }

    //endregion


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
