package com.CNAM.GeoRouting;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

/**
 * Created by fwoelffel on 22/05/14.
 */
public class CalendarTool {

    static boolean isBusy ( Context _context, int _calendarID ) {

        // Getting ContentResolver
        ContentResolver cr = _context.getContentResolver();

        // Getting all Events
        String projection[] = {CalendarContract.Events.CALENDAR_ID, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND, CalendarContract.Events.AVAILABILITY };;
        Uri events = CalendarContract.Events.CONTENT_URI;
        Cursor managedCursor = cr.query(events, projection, null, null, null);

        // Parsing Events
        if (managedCursor.moveToFirst()){
            do{
                int id = managedCursor.getInt(managedCursor.getColumnIndex(CalendarContract.Events.CALENDAR_ID));
                // Testing if parsed Event belongs to calendar designated by the given param
                if ( id == _calendarID) {
                    // Testing if parsed Event currently occurs
                    long begin = managedCursor.getLong(managedCursor.getColumnIndex(CalendarContract.Events.DTSTART));
                    long end = managedCursor.getLong(managedCursor.getColumnIndex(CalendarContract.Events.DTEND));
                    long current = System.currentTimeMillis();
                    if ( current >= begin && current <= end ) {
                        // Testing parsed Event's availability
                        int availability = managedCursor.getInt(managedCursor.getColumnIndex(CalendarContract.Events.AVAILABILITY));
                        if (availability == CalendarContract.Events.AVAILABILITY_BUSY) {
                            return true;
                        }
                    }
                }
            } while(managedCursor.moveToNext());
            managedCursor.close();
        }
        return false;
    }

}
