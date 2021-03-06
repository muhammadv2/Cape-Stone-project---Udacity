package com.muhammadv2.going_somewhere.model.data;


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

        // Trips table columns
        public static final String COLUMN_TRIP_NAME = "name";
        public static final String COLUMN_TIME_START = "start";
        public static final String COLUMN_TIME_END = "end";
        public static final String COLUMN_CITIES_NAMES = "cities";
        public static final String COLUMN_IMAGE_URL = "url";
    }


    /* Inner class that defines CityPlace table contents */
    public static final class PlaceEntry implements BaseColumns {
        public static final String TABLE_NAME = "places";

        //PlaceEntry content URI = base content URI + path(table name);
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        //places table columns
        public static final String COLUMN_PLACE_ID = "placeId";
        public static final String COLUMN_PLACE_NAME = "name";
        public static final String COLUMN_TRIP_ID = "trip";
    }

}
