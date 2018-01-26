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

import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.*;
import static com.muhammadv2.going_somewhere.utils.UriMatcherUtils.*;

public class TravelsProvider extends ContentProvider {

    // Help determine what kind of URI the provider receives and match it to an integer constant
    private static final UriMatcher sUriMatcher = buildUriMatcher();

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

        // Get access to the writable database creating SQLiteDatabase object
        final SQLiteDatabase db = mTravelsDbHelper.getWritableDatabase();

        // Find a matching uri using the helper method of UriMatcher
        int match = sUriMatcher.match(uri);

        Uri returnUri;
        // Switch between the different tables and insert into the one that matches received uri
        // Insert done on the whole table so the use case used will be only the directory
        switch (match) {
            case TRIPS:
                returnUri = tryToInsert(db, TripEntry.TABLE_NAME, values, TripEntry.CONTENT_URI);
                break;
            case CITIES:
                returnUri = tryToInsert(db, CityEntry.TABLE_NAME, values, CityEntry.CONTENT_URI);
                break;
            case PLACES:
                returnUri = tryToInsert(db, PlaceEntry.TABLE_NAME, values, PlaceEntry.CONTENT_URI);
                break;
            case NOTES:
                returnUri = tryToInsert(db, NoteEntry.TABLE_NAME, values, NoteEntry.CONTENT_URI);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        //notify resolver with the uri
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    /**
     * To help not duplicate code a method working for all insert cases into different tables
     * that accepts
     *
     * @param db        reference to the opened database
     * @param tableName to determine which table to insert too
     * @param values    which values to insert into that table
     */
    private Uri tryToInsert(SQLiteDatabase db, String tableName, ContentValues values, Uri contentUri) {

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
        // This application will display the whole table when needed so no need to query single ID
        switch (match) {
            case TRIPS:
                returnCursor = tryToQueryWholeTable(db, TripEntry.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            case CITIES:
                returnCursor = tryToQueryWholeTable(db, CityEntry.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            case PLACES:
                returnCursor = tryToQueryWholeTable(db, PlaceEntry.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            case NOTES:
                returnCursor = tryToQueryWholeTable(db, NoteEntry.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    /**
     * Helper method that try to query the passed table and return a cursor with data found
     *
     * @param db        reference to the opened database
     * @param tableName to determine which table to query
     */
    private Cursor tryToQueryWholeTable(SQLiteDatabase db, String tableName, String[] projection,
                                        String selection, String[] selectionArgs, String sortOrder) {
        return db.query(tableName,
                projection,
                selection,
                selectionArgs,
                null,
                null, sortOrder);
    }
    //endregion

    //region Delete
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        // Get access to the writable database creating SQLiteDatabase object
        final SQLiteDatabase db = mTravelsDbHelper.getWritableDatabase();

        // Find a matching uri using the helper method of UriMatcher
        int match = sUriMatcher.match(uri);

        int rowsDeleted;
        // Switch between the different tables and delete the row that matches received uri
        // This application will only delete one row at a time so the use case of row with ID is the only used
        switch (match) {
            case TRIP_WITH_ID:
                rowsDeleted = tryToDeleteOneRow(db, uri, TripEntry.TABLE_NAME);
                break;
            case CITY_WITH_ID:
                rowsDeleted = tryToDeleteOneRow(db, uri, CityEntry.TABLE_NAME);
                break;
            case PLACE_WITH_ID:
                rowsDeleted = tryToDeleteOneRow(db, uri, PlaceEntry.TABLE_NAME);
                break;
            case NOTE_WITH_ID:
                rowsDeleted = tryToDeleteOneRow(db, uri, NoteEntry.TABLE_NAME);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        if (rowsDeleted != 0) {
            // delete done Successfully , notify the resolver
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    /**
     * Helper method that try to delete a row form  the passed table and return an int representing
     * the deleted rows
     *
     * @param db        reference to the opened database
     * @param uri       passed uri to extract the selection and selectionArgs
     * @param tableName to determine which table to query
     */
    private int tryToDeleteOneRow(SQLiteDatabase db, @NonNull Uri uri, String tableName) {

        //Using selection and selectionArgs to specify which row to delete
        String id = uri.getPathSegments().get(1);
        String mSelection = "_id=?";
        String[] mSelectionArgs = {id};

        return db.delete(tableName, mSelection, mSelectionArgs);
    }
    //endregion

    //region Update
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        // Get access to the writable database creating SQLiteDatabase object
        final SQLiteDatabase db = mTravelsDbHelper.getWritableDatabase();

        // Find a matching uri using the helper method of UriMatcher
        int match = sUriMatcher.match(uri);

        int rowsUpdated;
        // Switch between the different tables and update the row that matches received uri
        // Update done on a single row so the use case used will be only the row with ID
        switch (match) {
            case TRIP_WITH_ID:
                rowsUpdated = tryToUpdateRow(db, uri, values, TripEntry.TABLE_NAME);
                break;
            case CITY_WITH_ID:
                rowsUpdated = tryToUpdateRow(db, uri, values, CityEntry.TABLE_NAME);
                break;
            case PLACE_WITH_ID:
                rowsUpdated = tryToUpdateRow(db, uri, values, PlaceEntry.TABLE_NAME);
                break;
            case NOTE_WITH_ID:
                rowsUpdated = tryToUpdateRow(db, uri, values, NoteEntry.TABLE_NAME);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        if (rowsUpdated != 0) {
            // Update done Successfully , notify the resolver
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    /**
     * Helper method that try to delete a row form  the passed table and return an int representing
     * the deleted rows
     *
     * @param db        reference to the opened database
     * @param uri       passed uri to extract the selection and selectionArgs
     * @param values
     * @param tableName to determine which table to query
     */
    private int tryToUpdateRow(SQLiteDatabase db, @NonNull Uri uri,
                               ContentValues values, String tableName) {

        //Using selection and selectionArgs to specify which row to update
        String id = uri.getPathSegments().get(1);
        String mSelection = "_id=?";
        String[] mSelectionArgs = {id};

        return db.update(tableName, values, mSelection, mSelectionArgs);
    }
    //endregion

    /**
     * Its okay for our application to not implement this method
     */
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
