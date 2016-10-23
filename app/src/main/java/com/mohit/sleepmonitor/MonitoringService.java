package com.mohit.sleepmonitor;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

/**
 * Created by Mohit on 23-10-2016.
 */
public class MonitoringService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    public static final int REQUEST_ROUTINE_SERVICE = 0;
    public static final int MONITOR_INTERVAL = 1000 * 60;
    private GoogleApiClient mApiClient; // hold reference to api client

    public MonitoringService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the GoogleApiClient, and
        // register my liseners to the GoogleApiClient member instance
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Connect to Google Play Services
        mApiClient.connect();
        Toast.makeText(getApplicationContext(), "Sleep Monitoring Started", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mApiClient.disconnect();
        Toast.makeText(getApplicationContext(), "Sleep Monitoring Stopped", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Create pending intent to start my IntentService
        Intent regularIntent = new Intent(MonitoringService.this, RoutineRecognizeService.class);
        PendingIntent pIntent = PendingIntent.getService(MonitoringService.this, REQUEST_ROUTINE_SERVICE, regularIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, MONITOR_INTERVAL, pIntent);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
    }
}
