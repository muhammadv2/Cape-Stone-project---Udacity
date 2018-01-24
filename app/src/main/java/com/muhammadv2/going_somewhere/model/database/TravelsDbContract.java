package com.muhammadv2.going_somewhere.model.database;


import android.provider.BaseColumns;

public final class TravelsDbContract {

    //to prevent someone from accidentally instantiate the contract class.
    private TravelsDbContract() {
    }

    /* Inner class that defines Trip table contents */
    public static final class TripEntry implements BaseColumns {
        public static final String TABLE_NAME = "trips";
        public static final String COLUMN_TRIP_NAME = "name";
        public static final String COLUMN_TIME_START = "start";
        public static final String COLUMN_TIME_END = "end";
    }


    /* Inner class that defines City table contents */
    static final class CityEntry implements BaseColumns {
        static final String TABLE_NAME = "cities";
        static final String COLUMN_CITY_NAME = "name";
        static final String TRIP_ID = "trip";
    }


    /* Inner class that defines Place table contents */
    static final class PlaceEntry implements BaseColumns {
        static final String TABLE_NAME = "places";
        static final String COLUMN_PLACE_NAME = "name";
        static final String COLUMN_TIME_START = "start";
        static final String COLUMN_TIME_END = "end";
        static final String COLUMN_CITY_ID = "city";
    }


    /* Inner class that defines Note table contents */
    static final class NoteEntry implements BaseColumns {
        static final String TABLE_NAME = "notes";
        static final String COLUMN_NOTE_TITLE = "title";
        static final String COLUMN_IS_TOGGLE_NOTE = "toggle";
    }

}
