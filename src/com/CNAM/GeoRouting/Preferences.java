package com.CNAM.GeoRouting;

public interface Preferences {

    public final static String AUTO             = "auto";
    public final static String GPS              = "gps";
    public final static String LOGIN            = "login";
    public final static String APPNAME          = "GeoRoutingApp";
    public final static String CREDENTIAL       = "credential";
    public final static String CALENDAR         = "calendar";

    public void loadPreferences();
    public void savePreferences();
}
