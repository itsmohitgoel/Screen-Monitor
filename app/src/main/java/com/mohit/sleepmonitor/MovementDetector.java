package com.mohit.sleepmonitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public MovementDetector(Context context) {
        //Create the database helper
        mOpenHelper = new SleepMonitorDbHelper(context);
    }

    public void onDetected(int movementType, int confidence) {
        long currentTime = System.currentTimeMillis() / 1000;

        if (movementType == DetectedActivity.ON_FOOT || movementType == DetectedActivity.WALKING) {

            //If it is first movement, then insert it otherwise ignore
            if (mMovementRowId == 0) {
                addMovement(currentTime);
            }
        }

        if (movementType == DetectedActivity.STILL) {
            // If User has moved recently, then this means he is back to sleep
            // so we'll set current time as end time of the movement
            updateMovement(currentTime);
        }
    }

    private void addMovement(long moveStartTime) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MovementEntry.COLUMN_DATE, moveStartTime);
        cv.put(MovementEntry.COLUMN_START, moveStartTime);

        long _id = db.insert(MovementEntry.TABLE_NAME, null, cv);
        if (_id > 0) {
            mMovementRowId = _id;
            mMoveStartTime = moveStartTime;
        } else {
            mMovementRowId = _id;
            mMoveStartTime = 0;
        }
    }

    private void updateMovement(long moveEndTime) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long durationInSeconds = (moveEndTime - mMoveStartTime);

        ContentValues cv = new ContentValues();
        cv.put(MovementEntry.COLUMN_END, moveEndTime);
        cv.put(MovementEntry.COLUMN_DURATION, durationInSeconds);

        int updateCount = db.update(MovementEntry.TABLE_NAME, cv, MovementEntry._ID + " = ?",
                new String[]{Long.toString(mMovementRowId)});

        if (updateCount != 0) {
            mMovementRowId = 0;
            mMoveStartTime = 0;
        }
    }


    private void queryMovements() {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(MovementEntry.TABLE_NAME, null, null, null, null, null, null);
    }

    private void deleteHistory() {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.delete(MovementEntry.TABLE_NAME, null, null);

        mMovementRowId = 0;
        mMoveStartTime = 0;
    }
}
