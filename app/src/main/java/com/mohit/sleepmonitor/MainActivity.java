package com.mohit.sleepmonitor;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    public static final int REQUEST_ROUTINE_SERVICE = 0;
    public static final int MONITOR_INTERVAL = 1000 * 60;
    private GoogleApiClient mApiClient; // hold reference to api client

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the GoogleApiClient, and
        // register my liseners to the GoogleApiClient member instance
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // Connect to Google Play Services
        mApiClient.connect();

        //Implement Stetho library
        Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(this);
        initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
        initializerBuilder.enableDumpapp(Stetho.defaultDumperPluginsProvider(this));
        Stetho.Initializer initializer = initializerBuilder.build();
        Stetho.initialize(initializer);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Create pending intent to start my IntentService
        Intent regularIntent = new Intent(MainActivity.this, RoutineRecognizeService.class);
        PendingIntent pIntent = PendingIntent.getService(MainActivity.this, REQUEST_ROUTINE_SERVICE, regularIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, MONITOR_INTERVAL, pIntent);

        //  mApiClient.disconnect();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
    }
}
