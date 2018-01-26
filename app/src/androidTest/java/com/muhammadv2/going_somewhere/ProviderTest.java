package com.muhammadv2.going_somewhere;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

import com.muhammadv2.going_somewhere.model.data.TravelsProvider;

import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.*;


public class ProviderTest extends ProviderTestCase2<TravelsProvider> {

    public ProviderTest() {
        super(TravelsProvider.class, AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    //region insertTest
    public void testInsert() {

        //test values
        String testName = "test name";
        int testInt = 777;

        Uri result;
        //method that creates ContentValues object and return the result uri
        result = tryInsertIntoTripsTable(testName, testInt);

        assertNotNull("The returned uri is null", result);

        long id = ContentUris.parseId(result);
        assertTrue("Error Inserting values into the table", id > 0);
    }

    private Uri tryInsertIntoTripsTable(String testName, int testInt) {
        Uri uri = TripEntry.CONTENT_URI;

        ContentValues cv = new ContentValues();
        cv.put(TripEntry.COLUMN_TRIP_NAME, testName);
        cv.put(TripEntry.COLUMN_TIME_START, testInt);
        cv.put(TripEntry.COLUMN_TIME_END, testInt);

        return getMockContentResolver().insert(uri, cv);
    }

    //endregion

    //region queryTest
    public void testQuery() {

        Uri uri = PlaceEntry.CONTENT_URI;

        Cursor cursor = getMockContentResolver()
                .query(uri, null, null, null, null);


        assertNotNull("returned cursor is empty", cursor);

        assertTrue("cursor has no data", cursor.moveToNext());
    }
    //endregion

}
