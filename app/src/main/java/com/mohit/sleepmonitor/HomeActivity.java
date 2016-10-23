package com.mohit.sleepmonitor;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mohit.sleepmonitor.data.SleepMonitorContract;
import com.mohit.sleepmonitor.data.SleepMonitorDbHelper;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private boolean isMonitoring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Button startButton = (Button) findViewById(R.id.btn_start);
        final Button stopButton = (Button) findViewById(R.id.btn_stop);
        Button showAnalysisButton = (Button) findViewById(R.id.btn_show_analysis);

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        isMonitoring = sp.getBoolean(getString(R.string.monitoring_key), false);

        if (!isMonitoring) {
            startButton.setVisibility(View.VISIBLE);
            stopButton.setVisibility(View.GONE);
        } else {
            stopButton.setVisibility(View.VISIBLE);
            startButton.setVisibility(View.GONE);
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MonitoringService.class);
                startService(intent);

                stopButton.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.GONE);
                sp.edit().putBoolean(getString(R.string.monitoring_key), true).commit();
                isMonitoring = true;
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MonitoringService.class);
                stopService(intent);

                startButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.GONE);
                sp.edit().putBoolean(getString(R.string.monitoring_key), false).commit();
                isMonitoring = false;
            }
        });

        showAnalysisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeActivity.this.isMonitoring) {
                    Toast.makeText(HomeActivity.this, R.string.monitoring_message, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear_history:
                SQLiteOpenHelper openHelper = new SleepMonitorDbHelper(this);
                SQLiteDatabase db = openHelper.getReadableDatabase();

                int deleteCount = db.delete(SleepMonitorContract.MovementEntry.TABLE_NAME, null, null);
                if (deleteCount > 0) {
                    Toast.makeText(HomeActivity.this, "History Cleared", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_dummy_data:
                insertDummyData();
                break;
        }
        return true;
    }

    private void insertDummyData() {
        SQLiteOpenHelper openHelper = new SleepMonitorDbHelper(this);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ArrayList<ContentValues> cvArray = new ArrayList<>();
        long moveStartTime = 1477195644;
        long moveEndTime = 1477195744;

        for (int i = 0; i < 10; i++) {
            ContentValues cv = new ContentValues();
            int duration = (int) (moveEndTime - moveStartTime);

            cv.put(SleepMonitorContract.MovementEntry.COLUMN_DATE, moveStartTime);
            cv.put(SleepMonitorContract.MovementEntry.COLUMN_START, moveStartTime);
            cv.put(SleepMonitorContract.MovementEntry.COLUMN_END, moveEndTime);
            cv.put(SleepMonitorContract.MovementEntry.COLUMN_DURATION, duration);
            cvArray.add(cv);

            moveStartTime = moveStartTime + 150;
            moveEndTime = moveStartTime + (150 * i);

        }

        for (ContentValues cv : cvArray) {
            long _id = db.insert(SleepMonitorContract.MovementEntry.TABLE_NAME, null, cv);
        }
    }
}
