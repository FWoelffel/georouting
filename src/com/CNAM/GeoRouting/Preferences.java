package com.CNAM.GeoRouting;

/**
 * Created by fwoelffel on 27/04/14.
 */
public interface Preferences {

    public final static String AUTO             = "auto";
    public final static String GPS              = "gps";
    public final static String NOTIFICATIONS    = "notifications";
    public final static String LOGIN            = "login";
    public final static String APPNAME          = "GeoRoutingApp";
    public final static String TOKEN            = "token";

    public void loadPreferences();
    public void savePreferences();

    // TODO Handle connection token
    // TODO Handle calendar
}
