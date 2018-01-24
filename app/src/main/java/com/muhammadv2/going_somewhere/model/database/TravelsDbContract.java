package com.muhammadv2.going_somewhere.model.database;


import android.provider.BaseColumns;

public final class TravelsDbContract {

    //to prevent someone from accidentally instantiate the contract class.
    private TravelsDbContract() {
    }

    /* Inner class that defines Trip table contents */
    public static class TripEntry implements BaseColumns {
        public static final String TABLE_NAME = "trips";
        public static final String COLUMN_TRIP_NAME = "name";
        public static final String COLUMN_TIME_START = "start";
        public static final String COLUMN_TIME_END = "end";
    }


    /* Inner class that defines City table contents */
    public static class CityEntry implements BaseColumns {
        public static final String TABLE_NAME = "cities";
        public static final String COLUMN_CITY_NAME = "name";
        public static final String TRIP_ID = "trip";
    }


    /* Inner class that defines Place table contents */
    public static class PlaceEntry implements BaseColumns {
        public static final String TABLE_NAME = "places";
        public static final String COLUMN_PLACE_NAME = "name";
        public static final String COLUMN_TIME_START = "start";
        public static final String COLUMN_TIME_END = "end";
        public static final String COLUMN_CITY_ID = "city";
    }


    /* Inner class that defines Note table contents */
    public static class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NOTE_TITLE = "title";
        public static final String COLUMN_IS_TOGGLE_NOTE = "toggle";
    }

}
