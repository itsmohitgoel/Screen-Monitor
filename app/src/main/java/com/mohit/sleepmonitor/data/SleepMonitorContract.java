package com.mohit.sleepmonitor.data;

import android.provider.BaseColumns;

/**
 * Created by Mohit on 22-10-2016.
 */
public class SleepMonitorContract {

    /* Inner class that defines the table contents of the movement table */
    public static final class MovementEntry implements BaseColumns {

        public static final String TABLE_NAME = "movement";

        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_DATE = "date";

        // start time of the movement during sleep, as per API
        public static final String COLUMN_START = "start";

        // end time of the movement during sleep, as per API
        public static final String COLUMN_END = "end";

        // movement block duration in seconds, which is end - start
        public static final String COLUMN_DURATION = "duration";
    }
}
