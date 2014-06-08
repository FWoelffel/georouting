package com.CNAM.GeoRouting;

public interface Preferences {

    public final static String AUTO             = "auto";
    public final static String GPS              = "gps";
    public final static String LOGIN            = "login";
    public final static String APPNAME          = "GeoRoutingApp";
    public final static String CREDENTIAL       = "credential";
    public final static String CALENDAR         = "calendar";

    public final static String CALENDAR_EVENING         = "calendar_evening";
    public final static String CALENDAR_WEEKEND         = "calendar_weekend";
    public final static String CALENDAR_BUSY            = "calendar_busy";

    public final static String GPS_LAT         = "gps_lat";
    public final static String GPS_LON         = "gps_lon";

    public void loadPreferences();
    public void savePreferences();
}
