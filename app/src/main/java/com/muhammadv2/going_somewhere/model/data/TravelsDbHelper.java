package com.muhammadv2.going_somewhere.model.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.*;


public class TravelsDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "travels.db";

    public TravelsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // String that creates Trip table with the needed columns
    private static final String CREATE_TRIP_TABLE =
            "CREATE TABLE " + TripEntry.TABLE_NAME + " (" +
                    TripEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TripEntry.COLUMN_TRIP_NAME + " TEXT NOT NULL," +
                    TripEntry.COLUMN_TIME_START + " INTEGER NOT NULL," +
                    TripEntry.COLUMN_TIME_END + " INTEGER NOT NULL)";


    // String that creates City table with the needed columns
    private static final String CREATE_CITY_TABLE =
            "CREATE TABLE " + CityEntry.TABLE_NAME + " (" +
                    CityEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CityEntry.COLUMN_CITY_NAME + " TEXT NOT NULL," +
                    CityEntry.COLUMN_TRIP_ID + " INTEGER NOT NULL)";


    // String that creates Place table with the needed columns
    private static final String CREATE_PLACE_TABLE =
            "CREATE TABLE " + PlaceEntry.TABLE_NAME + " (" +
                    PlaceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PlaceEntry.COLUMN_PLACE_NAME + " TEXT NOT NULL," +
                    PlaceEntry.COLUMN_CITY_ID + " INTEGER NOT NULL," +
                    PlaceEntry.COLUMN_TIME_START + " INTEGER NOT NULL," +
                    PlaceEntry.COLUMN_TIME_END + " INTEGER NOT NULL)";


    // String that creates Note table with the needed columns
    private static final String CREATE_NOTE_TABLE =
            "CREATE TABLE " + NoteEntry.TABLE_NAME + " (" +
                    NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NoteEntry.COLUMN_IS_TOGGLE_NOTE + " INTEGER NOT NULL," +
                    NoteEntry.COLUMN_NOTE_TITLE + " TEXT," +
                    NoteEntry.COLUMN_NOTE_BODY + " TEXT NOT NULL)";


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //calling execSQL method on the SQLiteDatabase object to create our four tables
        sqLiteDatabase.execSQL(CREATE_TRIP_TABLE);
        sqLiteDatabase.execSQL(CREATE_CITY_TABLE);
        sqLiteDatabase.execSQL(CREATE_PLACE_TABLE);
        sqLiteDatabase.execSQL(CREATE_NOTE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //no upgrades yet
    }
}
