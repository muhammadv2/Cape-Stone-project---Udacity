package com.muhammadv2.going_somewhere.model.database;


import android.net.Uri;
import android.provider.BaseColumns;

public final class TravelsDbContract {

    private static final String SCHEME = "content://";

    //The authority, which is how your code knows which Content Provider to access.
    public static final String AUTHORITY = "com.muhammadv2.going_somewhere";

    //The base content URI = "scheme + authority"
    private static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    //to prevent someone from accidentally instantiate the contract class.
    private TravelsDbContract() {
    }

    /* Inner class that defines Trip table contents */
    public static final class TripEntry implements BaseColumns {
        public static final String TABLE_NAME = "trips";

        //TripEntry content URI = base content URI + path(table name);
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String COLUMN_TRIP_NAME = "name";
        public static final String COLUMN_TIME_START = "start";
        public static final String COLUMN_TIME_END = "end";
    }


    /* Inner class that defines City table contents */
    static final class CityEntry implements BaseColumns {
        static final String TABLE_NAME = "cities";

        //CityEntry content URI = base content URI + path(table name);
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        static final String COLUMN_CITY_NAME = "name";
        static final String TRIP_ID = "trip";
    }


    /* Inner class that defines Place table contents */
    static final class PlaceEntry implements BaseColumns {
        static final String TABLE_NAME = "places";

        //PlaceEntry content URI = base content URI + path(table name);
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        static final String COLUMN_PLACE_NAME = "name";
        static final String COLUMN_TIME_START = "start";
        static final String COLUMN_TIME_END = "end";
        static final String COLUMN_CITY_ID = "city";
    }


    /* Inner class that defines Note table contents */
    static final class NoteEntry implements BaseColumns {
        static final String TABLE_NAME = "notes";

        //NoteEntry content URI = base content URI + path(table name);
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        static final String COLUMN_NOTE_TITLE = "title";
        static final String COLUMN_NOTE_BODY = "body";
        static final String COLUMN_IS_TOGGLE_NOTE = "toggle";
    }

}
