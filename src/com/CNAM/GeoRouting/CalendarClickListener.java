package com.CNAM.GeoRouting;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by fwoelffel on 16/05/14.
 */
public class CalendarClickListener implements AdapterView.OnItemClickListener {

    CalendarAdapter m_adapter;

    public CalendarClickListener(CalendarAdapter _adapter)
    {
        m_adapter = _adapter;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        m_adapter.setActivated(i);
        m_adapter.notifyDataSetChanged();
    }
}
