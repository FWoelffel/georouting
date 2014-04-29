package com.CNAM.GeoRouting;

public interface Preferences {

    public final static String AUTO             = "auto";
    public final static String GPS              = "gps";
    public final static String NOTIFICATIONS    = "notifications";
    public final static String LOGIN            = "login";
    public final static String APPNAME          = "GeoRoutingApp";
    public final static String TOKEN            = "token";
    public final static String CALENDAR         = "calendar";
    public final static String PROFILE          = "profile";

    public void loadPreferences();
    public void savePreferences();

    // TODO Handle connection token
    // TODO Handle calendar
}
