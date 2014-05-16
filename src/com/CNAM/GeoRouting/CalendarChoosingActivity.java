package com.CNAM.GeoRouting;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by fwoelffel on 15/05/14.
 */
public class CalendarChoosingActivity extends Activity implements Preferences{

    private SharedPreferences m_sharedPrefs;
    private TextView m_textview_calendartest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendarchoosing);

        m_sharedPrefs = getSharedPreferences(Preferences.APPNAME, 0);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void loadPreferences() {

    }

    @Override
    public void savePreferences() {

    }
}