package com.muhammadv2.going_somewhere.model.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.muhammadv2.going_somewhere.App;

import java.util.Arrays;

import javax.inject.Inject;

import timber.log.Timber;

import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.PlaceEntry;
import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.TripEntry;
import static com.muhammadv2.going_somewhere.utils.UriMatcherUtils.PLACES;
import static com.muhammadv2.going_somewhere.utils.UriMatcherUtils.PLACE_WITH_ID;
import static com.muhammadv2.going_somewhere.utils.UriMatcherUtils.TRIPS;
import static com.muhammadv2.going_somewhere.utils.UriMatcherUtils.TRIP_WITH_ID;
import static com.muhammadv2.going_somewhere.utils.UriMatcherUtils.buildUriMatcher;

public class TravelsProvider extends ContentProvider {

    // Help determine what kind of URI the provider receives and match it to an integer constant
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    //This class gets TravelsDpHelper through DI.
    @Inject
    TravelsDbHelper mTravelsDbHelper;

    SQLiteDatabase readDb;
    SQLiteDatabase writeDb;

    @Override
    public boolean onCreate() {
        // Instantiate TravelsDbHelper using the inject method on AppComponent interface using
        // the instance of Application
        App.getInstance().getAppComponent().inject(this);
        Timber.plant(new Timber.DebugTree());

        readDb = mTravelsDbHelper.getReadableDatabase();
        writeDb = mTravelsDbHelper.getWritableDatabase();
        return true;
    }

    //region Insert
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        // Find a matching uri using the helper method of UriMatcher
        int match = sUriMatcher.match(uri);

        Timber.d("insert uri " + uri);

        Uri returnUri;
        // Switch between the different tables and insert into the one that matches received uri
        // Insert done on the whole table so the use case used will be only the directory
        switch (match) {
            case TRIPS:
                returnUri = tryToInsert(TripEntry.TABLE_NAME, values, TripEntry.CONTENT_URI);
                break;
            case PLACES:
                returnUri = tryToInsert(PlaceEntry.TABLE_NAME, values, PlaceEntry.CONTENT_URI);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        Timber.d("return uri " + returnUri);
        Timber.d("return cv " + values.getAsString(TripEntry.COLUMN_TRIP_NAME));

        //notify resolver with the uri
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    /**
     * To help not duplicate code a method working for all insert cases into different tables
     * that accepts
     *
     * @param tableName to determine which table to insert too
     * @param values    which values to insert into that table
     */
    private Uri tryToInsert(String tableName, ContentValues values, Uri contentUri) {

        // Inserting values into the associated table
        long id = writeDb.insert(tableName, null, values);

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

        // Find a matching uri using the helper method of UriMatcher
        int match = sUriMatcher.match(uri);

        Cursor returnCursor;

        // Switch between the different tables and query the one that matches received uri
        // This application will display the whole table when needed so no need to query single ID
        switch (match) {
            case TRIPS:
                returnCursor = tryToQueryWholeTable(TripEntry.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;

            case PLACES:
                returnCursor = tryToQueryWholeTable(PlaceEntry.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            case PLACE_WITH_ID:
                returnCursor = tryToQuerySpecificItems(uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    private Cursor tryToQuerySpecificItems(Uri uri) {
        //Using selection and selectionArgs to specify which row to query

        String id = uri.getPathSegments().get(1);
        String mSelection = PlaceEntry.COLUMN_TRIP_ID + " = ?";
        String[] mSelectionArgs = new String[]{id};

        return readDb.query(PlaceEntry.TABLE_NAME,
                null,
                mSelection,
                mSelectionArgs,
                null,
                null,
                null);

    }

    /**
     * Helper method that try to query the passed table and return a cursor with data found
     *
     * @param tableName to determine which table to query
     */
    private Cursor tryToQueryWholeTable(String tableName, String[] projection,
                                        String selection, String[] selectionArgs, String sortOrder) {
        return readDb.query(tableName,
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

        // Find a matching uri using the helper method of UriMatcher
        int match = sUriMatcher.match(uri);

        int rowsDeleted;
        // Switch between the different tables and delete the row that matches received uri
        // This application will only delete one row at a time so the use case of row with ID is the only used
        switch (match) {
            case TRIP_WITH_ID:
                rowsDeleted = tryToDeleteOneRow(uri, TripEntry.TABLE_NAME);
                break;
            case PLACE_WITH_ID:
                rowsDeleted = tryToDeleteOneRow(uri, PlaceEntry.TABLE_NAME);
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
     * @param uri       passed uri to extract the selection and selectionArgs
     * @param tableName to determine which table to query
     */
    private int tryToDeleteOneRow(@NonNull Uri uri, String tableName) {

        //Using selection and selectionArgs to specify which row to delete
        String id = uri.getPathSegments().get(1);
        String mSelection = "_id=?";
        String[] mSelectionArgs = {id};

        return writeDb.delete(tableName, mSelection, mSelectionArgs);
    }
    //endregion

    //region Update
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        Timber.d("updated uri " + uri);

        // Find a matching uri using the helper method of UriMatcher
        int match = sUriMatcher.match(uri);

        int rowsUpdated;
        // Switch between the different tables and update the row that matches received uri
        // Update done on a single row so the use case used will be only the row with ID
        switch (match) {
            case TRIP_WITH_ID:
                rowsUpdated = tryToUpdateRow(uri, values, TripEntry.TABLE_NAME);
                break;
            case PLACE_WITH_ID:
                rowsUpdated = tryToUpdateRow(uri, values, PlaceEntry.TABLE_NAME);
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
     * @param uri       passed uri to extract the selection and selectionArgs
     * @param values    ContentValues holding the new data
     * @param tableName to determine which table to query
     */
    private int tryToUpdateRow(@NonNull Uri uri, ContentValues values, String tableName) {

        //Using selection and selectionArgs to specify which row to update

        String mSelection = TripEntry._ID + "=?";
        String[] mSelectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

        Timber.d("mselection " + mSelection + "selectionArgs " + Arrays.toString(mSelectionArgs));

        return writeDb.update(tableName, values, mSelection, mSelectionArgs);
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
