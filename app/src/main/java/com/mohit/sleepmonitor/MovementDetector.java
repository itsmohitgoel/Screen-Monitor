package com.mohit.sleepmonitor;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.google.android.gms.location.DetectedActivity;
import com.mohit.sleepmonitor.data.SleepMonitorContract.MovementEntry;
import com.mohit.sleepmonitor.data.SleepMonitorDbHelper;

/**
 * Created by Mohit on 22-10-2016.
 */
public class MovementDetector {
    private static final String LOG_TAG = MovementDetector.class.getSimpleName();
    private SleepMonitorDbHelper mOpenHelper;
    private long mMovementRowId = 0;
    private long mMoveStartTime = 0;
    private Context mContext;

    public MovementDetector(Context context) {
        //Create the database helper
        mOpenHelper = new SleepMonitorDbHelper(context);
        mContext = context;
    }

    /**
     * Callback method, which on receiving activity of (confidence > 50)
     * will perform the required action
     *
     * @param movementType
     * @param confidence
     */
    public void onDetected(int movementType, int confidence) {
        long currentTime = System.currentTimeMillis() / 1000;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        mMovementRowId = Long.valueOf(sp.getInt(mContext.getString(R.string.movement_key), 0));
        mMoveStartTime = Long.valueOf(sp.getLong(mContext.getString(R.string.start_time), 0));

        // Because this api work on low sensors, so we'll consider all below activities
        // as disturbance with in sleep, so will store the time block in db
        if (movementType == DetectedActivity.ON_FOOT || movementType == DetectedActivity.WALKING
                || (movementType == DetectedActivity.IN_VEHICLE) ||
                (movementType == DetectedActivity.ON_BICYCLE) ||
                (movementType == DetectedActivity.UNKNOWN)) {

            //If it is first movement activity, then insert it otherwise ignore
            // and keep listening for the first STILL DETECTION with confidence of 100
            if (mMovementRowId == 0) {
                addMovement(currentTime);
            }
        }

        if ((movementType == DetectedActivity.STILL) && (confidence == 100)) {
            // If User has moved recently, then this means he is back to sleep
            // so we'll set current time as end time of the movement,
            // along with interval of movement
            if (mMovementRowId > 0) {
                int interval = (int) (currentTime - mMoveStartTime);
                updateMovement(currentTime, interval, (int) mMovementRowId);
            }
        }
    }

    /**
     * Helper method to insert movement record in DB and shared preference
     *
     * @param moveStartTime start time of this movement
     */
    private void addMovement(long moveStartTime) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MovementEntry.COLUMN_DATE, moveStartTime);
        cv.put(MovementEntry.COLUMN_START, moveStartTime);

        long _id = db.insert(MovementEntry.TABLE_NAME, null, cv);
        if (_id > 0) {
            // Retain inserted movement ID in shared preferences
            storeMovementIdAndStartTime((int) _id, moveStartTime);
        }
    }

    /**
     * Helper method to  add end time of the latest movement being monitored,
     * along with duration in seconds
     *
     * @param moveEndTime end time of the movement
     * @param duration    interval for which movement happened
     * @param rowID       unique database id of the movement
     */
    private void updateMovement(long moveEndTime, int duration, int rowID) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MovementEntry.COLUMN_END, moveEndTime);
        cv.put(MovementEntry.COLUMN_DURATION, duration);

        int updateCount = db.update(MovementEntry.TABLE_NAME, cv, MovementEntry._ID + " = ?",
                new String[]{Integer.toString(rowID)});

        if (updateCount != 0) {
            storeMovementIdAndStartTime(0, 0);
        }
    }


    private void deleteHistory() {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.delete(MovementEntry.TABLE_NAME, null, null);

        storeMovementIdAndStartTime(0, 0);
    }

    private void storeMovementIdAndStartTime(int motionRowID, long startTime) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(mContext.getString(R.string.movement_key), motionRowID)
                .putLong(mContext.getString(R.string.start_time), startTime).commit();
    }
}
