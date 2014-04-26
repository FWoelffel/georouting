package com.CNAM.GeoRouting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.w3c.dom.ProcessingInstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainScreen extends Activity {

    private int dbg_count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dbg_populate(10);

        ((Switch)findViewById(R.id.switch_AutoMode)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ((ProfileListView)findViewById(R.id.listView_Profiles)).setActive(b);
            }
        });

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
