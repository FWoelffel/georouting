package com.CNAM.GeoRouting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;


public class MainActivity extends Activity implements Preferences {

    public final static int KILLAPP = 1000;

    private SharedPreferences m_sharedPrefs;

    private Switch m_switch_AutoMode;
    private ProfileListView m_profileListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        m_sharedPrefs = getSharedPreferences(Preferences.APPNAME, 0);

        m_switch_AutoMode = (Switch)findViewById(R.id.switch_AutoMode);
        m_profileListView = (ProfileListView)findViewById(R.id.listView_Profiles);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        // TODO Get real profile objects !!
        dbg_populate(10);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(m_sharedPrefs.getString(Preferences.LOGIN, "").equals("")) {
            startActivityForResult(new Intent(MainActivity.this, LogInActivity.class), 1);
        }
        loadPreferences();
    }

    @Override
    public void onPause() {
        savePreferences();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainactivityactions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == MainActivity.KILLAPP) {
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Deprecated
    private void dbg_populate (int _nb) {
        for(int i = 0; i < _nb; i++)
            try {
                m_profileListView.addProfile(new Profile("Name_" + i, "Description"));
            } catch (ProfileException e) {
                e.printStackTrace();
            }
        ((Profile)m_profileListView.getAdapter().getItem(0)).switchState();
    }

    @Override
    public void loadPreferences() {
        SharedPreferences prefs = getSharedPreferences(Preferences.APPNAME, 0);
        m_profileListView.setActivated(prefs.getBoolean(Preferences.AUTO, false));
        m_switch_AutoMode.setChecked(prefs.getBoolean(Preferences.AUTO, false));
    }

    @Override
    public void savePreferences() {
        SharedPreferences.Editor editor = m_sharedPrefs.edit();
        editor.putBoolean(Preferences.AUTO, m_switch_AutoMode.isChecked());
        editor.commit();
    }
}