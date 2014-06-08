package com.CNAM.GeoRouting;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fwoelffel on 08/06/14.
 */
public class BackgroundService extends Service
{
    private static final String TAG = BackgroundService.class.getSimpleName();
    private static final long UPDATE_INTERVAL = 1 * 15 * 100;
    private static final long DELAY_INTERVAL = 0;

    private Timer timer;

    public BackgroundService() {
        timer = new Timer();
    }

    public void onCreate() {
        Toast.makeText(this, "Started!", Toast.LENGTH_LONG);
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        timer.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        Log.d(TAG, "MY TASK IS RUNNING");
                    }
                },
                DELAY_INTERVAL,
                UPDATE_INTERVAL
        );

        super.onStartCommand(intent, flags, startId);

        return 0;
    }

    @Override
    public void onDestroy() {
        timer.cancel();

        super.onDestroy();
    }
}