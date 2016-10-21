package com.mohit.sleepmonitor;

import android.app.IntentService;
import android.content.Intent;

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

    }
}
