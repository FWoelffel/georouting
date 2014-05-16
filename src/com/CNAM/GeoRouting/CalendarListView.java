package com.CNAM.GeoRouting;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by fwoelffel on 16/05/14.
 */
public class CalendarListView extends ListView {

    private CalendarAdapter m_adapter;
    private CalendarClickListener m_clickListener;

    public CalendarListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_adapter = new CalendarAdapter(context, new ArrayList<Calendar>());
        setAdapter(m_adapter);
        m_clickListener = new CalendarClickListener(m_adapter);
        setOnItemClickListener(m_clickListener);
    }
}
