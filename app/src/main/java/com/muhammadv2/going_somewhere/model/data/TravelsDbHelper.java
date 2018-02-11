package com.muhammadv2.going_somewhere.model.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.muhammadv2.going_somewhere.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.PlaceEntry;
import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.TripEntry;

@Singleton
public class TravelsDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "travels.db";

    @Inject
    public TravelsDbHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // String that creates Trip table with the needed columns
    private static final String CREATE_TRIP_TABLE =
            "CREATE TABLE " + TripEntry.TABLE_NAME + " ( " +
                    TripEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TripEntry.COLUMN_TRIP_NAME + " TEXT NOT NULL," +
                    TripEntry.COLUMN_CITIES_NAMES + " TEXT NOT NULL," +
                    TripEntry.COLUMN_IMAGE_URL + " TEXT," +
                    TripEntry.COLUMN_TIME_START + " INTEGER NOT NULL," +
                    TripEntry.COLUMN_TIME_END + " INTEGER NOT NULL);";


    // String that creates CityPlace table with the needed columns
    private static final String CREATE_PLACE_TABLE =
            "CREATE TABLE " + PlaceEntry.TABLE_NAME + " (" +
                    PlaceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PlaceEntry.COLUMN_PLACE_NAME + " TEXT NOT NULL," +
                    PlaceEntry.COLUMN_TRIP_ID + " INTEGER NOT NULL);";


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //calling execSQL method on the SQLiteDatabase object to create our four tables
        sqLiteDatabase.execSQL(CREATE_TRIP_TABLE);
        sqLiteDatabase.execSQL(CREATE_PLACE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //no upgrades yet
    }
}
