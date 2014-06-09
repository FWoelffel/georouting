package com.CNAM.GeoRouting;

public interface Preferences {

    public final static String AUTO             = "auto";
    public final static String GPS              = "gps";
    public final static String LOGIN            = "login";
    public final static String APPNAME          = "GeoRoutingApp";
    public final static String CREDENTIAL       = "credential";
    public final static String CALENDAR         = "calendar";

    public final static String CALENDAR_ID              = "calendar_id";
    public final static String CALENDAR_EVENING         = "calendar_evening";
    public final static String CALENDAR_WEEKEND         = "calendar_weekend";
    public final static String CALENDAR_BUSY            = "calendar_busy";
    public final static String CALENDAR_HOMEWORKING     = "calendar_homework";

    public final static String GPS_LAT         = "gps_lat";
    public final static String GPS_LON         = "gps_lon";
    public final static String GPS_SPEED_GT50KMH     = "gps_gt50kmh";
    public final static String GPS_DIST_GT2KM  = "gps_dist_gt2km";

    public final static String DEFAULT         = "default";

    public void loadPreferences();
    public void savePreferences();
}
