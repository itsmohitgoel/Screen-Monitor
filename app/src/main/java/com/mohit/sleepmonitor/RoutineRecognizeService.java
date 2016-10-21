package com.mohit.sleepmonitor;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

/**
 * Created by Mohit on 21-10-2016.
 */
public class RoutineRecognizeService extends IntentService {

    public RoutineRecognizeService(){
        super(RoutineRecognizeService.class.getSimpleName());
    }
    public RoutineRecognizeService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // if intent contains activity recognizition data, then
        // retrive list of detected activities
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            List<DetectedActivity> probableActivities = result.getProbableActivities();
        }
    }
}
