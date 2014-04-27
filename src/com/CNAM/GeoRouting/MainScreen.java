package com.CNAM.GeoRouting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;


public class MainScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dbg_populate(10);

        SharedPreferences prefs = getSharedPreferences(Preferences.APPNAME, 0);
        if(prefs.getString(Preferences.LOGIN, "").equals(""))
        {
            startActivity(new Intent(this, LogInActivity.class));
        }



        (findViewById(R.id.listView_Profiles)).setActivated(prefs.getBoolean(Preferences.AUTO, false));

        ((Switch)findViewById(R.id.switch_AutoMode)).setChecked(prefs.getBoolean(Preferences.AUTO, false));
        ((Switch)findViewById(R.id.switch_AutoMode)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences(Preferences.APPNAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(Preferences.AUTO, b);
                editor.commit();
            }
        });
    }

    public void onStop()
    {
        SharedPreferences settings = getSharedPreferences(Preferences.APPNAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Preferences.AUTO, ((Switch)findViewById(R.id.switch_AutoMode)).isChecked());
        editor.commit();

        super.onStop();
    }

    private void dbg_populate (int _nb) {
        ProfileListView p = (ProfileListView) findViewById(R.id.listView_Profiles);
        for(int i = 0; i < _nb; i++)
            try {
                p.addProfile(new Profile("Name_" + i, "Description"));
            } catch (ProfileException e) {
                e.printStackTrace();
            }
        ((Profile)p.getAdapter().getItem(0)).switchState();
    }
}
