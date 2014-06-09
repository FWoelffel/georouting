package com.CNAM.GeoRouting;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.*;
import java.util.Calendar;

/**
 * Created by fwoelffel on 08/06/14.
 */
public class BackgroundService extends Service implements Preferences
{
    private static final String TAG = BackgroundService.class.getSimpleName();
    private static final long UPDATE_INTERVAL = 15 * 1000;
    private static final long DELAY_INTERVAL = 0;

    SharedPreferences m_sharedPrefs;

    private Timer timer;

    public BackgroundService() {
        timer = new Timer();
    }

    public void onCreate() {
        m_sharedPrefs = getApplicationContext().getSharedPreferences(Preferences.APPNAME, 0);
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
                        if(m_sharedPrefs.getBoolean(Preferences.AUTO, false))
                        {
                            Log.d(TAG, "Applied profile's ID is : " + NetworkManager.getInstance().getAppliedProfileID());
                            int toApply = whatProfileShouldBeApplied();
                            NetworkManager.getInstance().setAppliedProfile(toApply);
                        }
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

    @Override
    public void loadPreferences() {

    }

    @Override
    public void savePreferences() {

    }

    public int whatProfileShouldBeApplied()
    {

        boolean gps_crit = m_sharedPrefs.getBoolean(Preferences.GPS, false);
        boolean cal_crit = m_sharedPrefs.getBoolean(Preferences.CALENDAR, false);

        int day = CalendarTool.getDay();
        Log.d(TAG, "\tDay : " + day);
        int hour = CalendarTool.getHour();
        Log.d(TAG, "\tHour : " + hour);
        boolean busy = CalendarTool.isBusy(getApplicationContext(), m_sharedPrefs.getInt(Preferences.CALENDAR_ID, -1));
        Log.d(TAG, "\tBusy : " + busy);

        if(cal_crit)
        {
            if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
                Log.d(TAG, "This profile should be applied : Weekend");
                return m_sharedPrefs.getInt(Preferences.CALENDAR_WEEKEND, 10);
            }
            if (hour > 20 && hour < 7) {
                Log.d(TAG, "This profile should be applied : Evening");
                return m_sharedPrefs.getInt(Preferences.CALENDAR_EVENING, 11);
            }
            if (busy) {
                Log.d(TAG, "This profile should be applied : Busy");
                return m_sharedPrefs.getInt(Preferences.CALENDAR_BUSY, 10);
            }
            if(gps_crit)
            {
                if (day == Calendar.WEDNESDAY && false) // TODO : distance > 2km
                {
                    Log.d(TAG, "This profile should be applied : HomeWorker");
                    return m_sharedPrefs.getInt(Preferences.CALENDAR_HOMEWORKING, 14);
                }
            }
            else
            {
                if (day == Calendar.WEDNESDAY)
                {
                    Log.d(TAG, "This profile should be applied : HomeWorker");
                    return m_sharedPrefs.getInt(Preferences.CALENDAR_HOMEWORKING, 14);
                }
            }
        }
        if(gps_crit)
        {
            if (false) // TODO : speed > 50kmh
            {
                Log.d(TAG, "This profile should be applied : OnTheRoad");
                return m_sharedPrefs.getInt(Preferences.GPS_SPEED_GT50KMH, 12);
            }

            if (false) // TODO : distance > 2km
            {
                Log.d(TAG, "This profile should be applied : OffSite");
                return m_sharedPrefs.getInt(Preferences.GPS_DIST_GT2KM, 13);
            }
        }

        Log.d(TAG, "This profile should be applied : Default");
        return m_sharedPrefs.getInt(Preferences.DEFAULT, 0);

    }

}