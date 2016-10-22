package com.mohit.sleepmonitor;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
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
    private MovementDetector mDetector;

    public RoutineRecognizeService() {
        super(RoutineRecognizeService.class.getSimpleName());

        mDetector = new MovementDetector(this);
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
            if (confidence > 50) {
            mDetector.onDetected(activity.getType(), confidence);
            }
        }
    }

    private void showToast(String msg, int confidence) {
        mHandler.post(new DisplayToast(msg, confidence));
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
}
