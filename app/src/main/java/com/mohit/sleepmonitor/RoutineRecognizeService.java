package com.mohit.sleepmonitor;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

/**
 * Created by Mohit on 21-10-2016.
 */
public class RoutineRecognizeService extends IntentService {
    public static final String LOG_TAG = RoutineRecognizeService.class.getSimpleName();
    private Handler mHandler = new Handler();

    public RoutineRecognizeService() {
        super(RoutineRecognizeService.class.getSimpleName());
    }

    public RoutineRecognizeService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // if intent contains activity recognizition data, then
        // retrive list of detected activities
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            List<DetectedActivity> probableActivities = result.getProbableActivities();

            handleDetectedActivities(probableActivities);
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        for (DetectedActivity activity : probableActivities) {
            int confidence = activity.getConfidence();
            switch (activity.getType()) {


                case DetectedActivity.IN_VEHICLE:
                    //Toast.makeText(this, "in Vehicle" + confidence, Toast.LENGTH_SHORT).show();
                   // showToast("Vehicle", confidence);
                    Log.i(LOG_TAG, "in Vehicle: " + confidence);
                    break;
                case DetectedActivity.ON_BICYCLE:
                    //Toast.makeText(this, "on Bicycle: " + confidence, Toast.LENGTH_SHORT).show();
                   // showToast("Bicycle", confidence);
                    Log.i(LOG_TAG, "on Bicycle: " + confidence);
                    break;
                case DetectedActivity.ON_FOOT:
                    //Toast.makeText(this, "on Foot: " + confidence, Toast.LENGTH_SHORT).show();
                    showToast("Foot", confidence);
                    Log.i(LOG_TAG, "on Foot: " + confidence);
                    break;
                case DetectedActivity.RUNNING:
                    //Toast.makeText(this, "Running: " + confidence, Toast.LENGTH_SHORT).show();
                    showToast("Running", confidence);
                    Log.i(LOG_TAG, "Running: " + confidence);
                    break;
                case DetectedActivity.STILL: {
                    showToast("Still", confidence);
                    //Toast.makeText(this, "Still: " + confidence, Toast.LENGTH_SHORT).show();
                    Log.i(LOG_TAG, "Still: " + confidence);
                    break;
                }
                case DetectedActivity.TILTING:
                    //Toast.makeText(this, "Tilting: " + confidence, Toast.LENGTH_SHORT).show();
                    showToast("Tilting", confidence);
                    Log.i(LOG_TAG, "Tilting: " + confidence);
                    break;
                case DetectedActivity.WALKING:
                    showToast("Walking", confidence);
                    //Toast.makeText(this, "Walking: " + confidence, Toast.LENGTH_SHORT).show();
                    Log.i(LOG_TAG, "Walking: " + confidence);
                    if (confidence >= 75) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                        builder.setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(getString(R.string.app_name))
                                .setContentText("Are you walking now?");

                        Notification notification = builder.build();
                    }
                    break;
                case DetectedActivity.UNKNOWN: {
                    showToast("Unknown", confidence);
                    // Toast.makeText(this, "Unknown: " + confidence, Toast.LENGTH_SHORT).show();
                    Log.e(LOG_TAG, "Unknown: " + confidence);
                    break;
                }
                default:
                    break;
            }
        }
    }

    class DisplayToast implements Runnable {

        String msg;
        int confidence;

        public DisplayToast(String message, int conf) {
            msg = message;
            confidence = conf;
        }

        @Override
        public void run() {
            if (confidence > 40)
                Toast.makeText(RoutineRecognizeService.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void showToast(String msg, int confidence) {
        mHandler.post(new DisplayToast(msg, confidence));
    }
}
