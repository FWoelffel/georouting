package com.CNAM.GeoRouting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends Activity implements Preferences {

    private SharedPreferences m_sharedPrefs;
    private Switch m_switch_AutoMode, m_switch_GPS, m_switch_calendar;
    private Button m_button_ChooseCalendar, m_button_ForgetMe;
    private View m_automatic_childs, m_calendar_childs, m_geographic_childs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        m_sharedPrefs = getSharedPreferences(Preferences.APPNAME, 0);
        m_switch_AutoMode = ((Switch)findViewById(R.id.switch_AutoMode));
        m_switch_GPS = ((Switch)findViewById(R.id.settings_switch_geo_criterias));
        m_switch_calendar = ((Switch)findViewById(R.id.settings_switch_calendar_criterias));
        m_button_ChooseCalendar = ((Button)findViewById(R.id.settings_button_choose_calendar));
        m_button_ForgetMe = ((Button)findViewById(R.id.button_disconnect));

        m_automatic_childs = findViewById(R.id.automatic_childs);
        m_geographic_childs = findViewById(R.id.geographic_childs);
        m_calendar_childs = findViewById(R.id.calendar_childs);

        m_button_ForgetMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetMe();
            }
        });

        m_button_ChooseCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, CalendarChoosingActivity.class));
            }
        });

        m_switch_AutoMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toggleContent(m_automatic_childs, b);
            }
        });

        m_switch_GPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toggleContent(m_geographic_childs, b);
            }
        });

        m_switch_calendar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toggleContent(m_calendar_childs, b);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPreferences();
    }

    @Override
    public void onPause() {
        savePreferences();
        super.onPause();
    }

    @Override
    public void loadPreferences() {
        ((TextView)findViewById(R.id.settings_textview_login_value)).setText(m_sharedPrefs.getString(Preferences.LOGIN, ""));
        m_switch_AutoMode.setChecked(m_sharedPrefs.getBoolean(Preferences.AUTO, false));
        toggleContent(m_automatic_childs, m_sharedPrefs.getBoolean(Preferences.AUTO, false));
        m_switch_GPS.setChecked(m_sharedPrefs.getBoolean(Preferences.GPS, false));
        toggleContent(m_geographic_childs, m_sharedPrefs.getBoolean(Preferences.GPS, false));
        m_switch_calendar.setChecked(m_sharedPrefs.getBoolean(Preferences.CALENDAR, false));
        toggleContent(m_calendar_childs, m_sharedPrefs.getBoolean(Preferences.CALENDAR, false));
    }

    @Override
    public void savePreferences() {
        SharedPreferences.Editor editor = m_sharedPrefs.edit();
        editor.putBoolean(Preferences.AUTO, m_switch_AutoMode.isChecked());
        editor.putBoolean(Preferences.GPS, m_switch_GPS.isChecked());
        editor.putBoolean(Preferences.CALENDAR, m_switch_calendar.isChecked());
        editor.commit();
    }

    private void forgetMe() {
        SharedPreferences.Editor editor = m_sharedPrefs.edit();
        editor.putString(Preferences.LOGIN, "");
        editor.putString(Preferences.CREDENTIAL, "");
        editor.putBoolean(Preferences.AUTO, false);
        editor.putBoolean(Preferences.GPS, false);
        editor.putBoolean(Preferences.CALENDAR, false);
        editor.commit();
        finish();
    }

    private void toggleContent(View v, boolean b)
    {
        VisualEffects.toggleContent(this, v, b);
    }
}