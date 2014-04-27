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
public class LogInActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        SharedPreferences prefs = getSharedPreferences(Preferences.APPNAME, 0);
        ((EditText)(findViewById(R.id.editText_UserName))).setText(prefs.getString(Preferences.LOGIN, ""), TextView.BufferType.EDITABLE);

        (findViewById(R.id.button_LogIn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onLogInBtnClick();
            }
        });
    }

    public void onLogInBtnClick()
    {
        String userName = ((TextView)findViewById(R.id.editText_UserName)).getText().toString();
        String userPassword = ((TextView)findViewById(R.id.editText_UserPassword)).getText().toString();
        String token = getToken(userName, userPassword);
        if ( !token.equals("") )
        {
            //TODO Store userName and close activity
            SharedPreferences prefs = getSharedPreferences(Preferences.APPNAME, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Preferences.LOGIN, userName);
            editor.putString(Preferences.TOKEN, token);
            editor.commit();
            this.finish();
        }
    }

    private String getToken(String _userName, String _userPassword)
    {
        //TODO Get token and store in shared prefs
        // Return token as a String if IDs are OK, else return ""
        return "MYBEAUTIFULTOKEN";
    }
}