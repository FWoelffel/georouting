package com.CNAM.GeoRouting;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by fwoelffel on 27/04/14.
 */
public class LogInActivity extends Activity implements Preferences {

    private SharedPreferences m_sharedPrefs;

    private Button m_button_LogIn;
    private TextView m_textview_UserName, m_textview_UserPassword;

    private String m_token;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        m_sharedPrefs = getSharedPreferences(Preferences.APPNAME, 0);
        m_token = "";

        m_button_LogIn = (Button)findViewById(R.id.button_LogIn);
        m_textview_UserName = (TextView)findViewById(R.id.editText_UserName);
        m_textview_UserPassword = (TextView)findViewById(R.id.editText_UserName);

        m_button_LogIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onLogInBtnClick();
            }
        });
    }

    public void onResume() {
        super.onResume();
        loadPreferences();
    }

    public void onLogInBtnClick()
    {
        String userName = m_textview_UserName.getText().toString();
        String userPassword = m_textview_UserPassword.getText().toString();
        m_token = getToken(userName, userPassword);
        if ( !m_token.equals("") )
        {
            savePreferences();
            this.finish();
        }
    }

    private String getToken(String _userName, String _userPassword)
    {
        //TODO Get token and store in shared prefs
        // Return token as a String if IDs are OK, else return ""
        return "MYBEAUTIFULTOKEN";
    }

    @Override
    public void loadPreferences() {
        SharedPreferences prefs = getSharedPreferences(Preferences.APPNAME, 0);
        ((EditText)(findViewById(R.id.editText_UserName))).setText(prefs.getString(Preferences.LOGIN, ""), TextView.BufferType.EDITABLE);
    }

    @Override
    public void savePreferences() {
        SharedPreferences.Editor editor = m_sharedPrefs.edit();
        editor.putString(Preferences.LOGIN, m_textview_UserName.getText().toString());
        editor.putString(Preferences.TOKEN, m_token);
        editor.commit();
    }
}