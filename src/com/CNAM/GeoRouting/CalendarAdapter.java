package com.CNAM.GeoRouting;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fwoelffel on 16/05/14.
 */
public class CalendarAdapter extends ArrayAdapter<Calendar> implements Preferences{

    private SharedPreferences m_sharedPrefs;
    private ContentResolver m_contentResolver;

    public CalendarAdapter(Context context, ArrayList<Calendar> objects) {
        super(context, R.layout.profilelayout, objects);
        m_sharedPrefs = context.getSharedPreferences(Preferences.APPNAME, 0);
        m_contentResolver = context.getContentResolver();

        loadCalendars();
        loadPreferences();
    }

    private void loadCalendars() {
        String projection[] = {CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME};
        Uri calendars;
        calendars = Uri.parse("content://com.android.calendar/calendars");
        Cursor managedCursor = m_contentResolver.query(calendars, projection, null, null, null);

        if (managedCursor != null && managedCursor.moveToFirst()){
            do{
                int id = managedCursor.getInt(managedCursor.getColumnIndex(CalendarContract.Calendars._ID));
                String name = managedCursor.getString(managedCursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME));
                add(new Calendar(id, name));
            } while(managedCursor.moveToNext());
            managedCursor.close();
        }
    }

    public ArrayList<Calendar> getCalendars () {
        ArrayList<Calendar> arr = new ArrayList<Calendar>();
        for(int i = 0; i < getCount(); i++) {
            arr.add(getItem(i));
        }
        return arr;
    }

    public Calendar getByID(int _calendarID) {
        for(Calendar p : getCalendars()) {
            if (p.getID() == _calendarID) {
                return p;
            }
        }
        return null;
    }

    public Calendar getActivated () {
        for(Calendar p : getCalendars()) {
            if (p.isActivated()) {
                return p;
            }
        }
        return null;
    }

    public void setActivated(int _index) {
        setActivated(getItem(_index));
    }

    public void setActivated (Calendar _calendar) {
        if (_calendar != null) {
            for(Calendar p : getCalendars()) {
                p.setActivated(false);
            }
            _calendar.setActivated(true);
            savePreferences();
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.calendarlayout, parent, false);

        TextView name = (TextView) rowView.findViewById(R.id.name);
        name.setText(getItem(position).getName());

        ImageView state = (ImageView) rowView.findViewById(R.id.state);
        if (getItem(position).isActivated())
            state.setVisibility(View.VISIBLE);
        else
            state.setVisibility(View.INVISIBLE);

        return rowView;
    }

    @Override
    public void loadPreferences() {
        int calendarID = m_sharedPrefs.getInt(Preferences.CALENDAR, 0);
        if (calendarID == 0) {
            getItem(0).setActivated(true);
            savePreferences();
        }
        else {
            getByID(calendarID).setActivated(true);
        }
    }

    @Override
    public void savePreferences() {
        SharedPreferences.Editor editor = m_sharedPrefs.edit();
        editor.putInt(Preferences.CALENDAR, getActivated().getID());
        editor.commit();
    }
}
