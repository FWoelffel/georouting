package com.CNAM.GeoRouting;

import android.view.View;
import android.widget.AdapterView;

public class ProfileClickListener implements AdapterView.OnItemClickListener {

    ProfileAdapter m_adapter;

    public ProfileClickListener(ProfileAdapter _adapter)
    {
        m_adapter = _adapter;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!m_adapter.getItem(i).equals(m_adapter.getActivated())) {
            m_adapter.setActivated(i);
            m_adapter.notifyDataSetChanged();
        }
    }
}
