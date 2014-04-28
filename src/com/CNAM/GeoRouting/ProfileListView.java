package com.CNAM.GeoRouting;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import java.util.ArrayList;

public class ProfileListView extends ListView {

    private boolean m_active;
    private ProfileAdapter m_adapter;
    private ProfileClickListener m_clickListener;

    public ProfileListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_adapter = new ProfileAdapter(context, new ArrayList<Profile>());
        setAdapter(m_adapter);
        m_clickListener = new ProfileClickListener(m_adapter);
        setOnItemClickListener(m_clickListener);
    }

    public boolean addProfile (Profile _profile) {
        boolean b = m_adapter.addProfile(_profile);
        if (b)
            m_adapter.notifyDataSetChanged();
        return b;
    }

    public void setActive (boolean _active) {
        m_active = _active;
    }

    public boolean isActive() {
        return m_active;
    }
}
