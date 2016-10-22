package com.mohit.sleepmonitor.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mohit.sleepmonitor.data.SleepMonitorContract.MovementEntry;
/**
 * Created by Mohit on 22-10-2016.
 */
public class SleepMonitorDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "sleepmonitor.db";

    public SleepMonitorDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVEMENT_TABLE = "CREATE TABLE " + MovementEntry.TABLE_NAME + " (" +
                MovementEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                MovementEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                MovementEntry.COLUMN_START + " INTEGER NOT NULL, " +
                MovementEntry.COLUMN_END + " INTEGER DEFAULT 0, " +
                MovementEntry.COLUMN_DURATION + " INTEGER DEFAULT 0); ";

        db.execSQL(SQL_CREATE_MOVEMENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // As this table is meant to act as a cache we will drop it on schema change
        db.execSQL(" DROP TABLE IF EXISS " + MovementEntry.TABLE_NAME);
        onCreate(db);
    }
}
