package com.CNAM.GeoRouting;

/**
 * Created by fwoelffel on 16/05/14.
 */
public class Calendar {
    private int m_ID;
    private String m_name;
    private boolean m_activated;

    public Calendar(int _ID, String _name) {
        this.m_ID = _ID;
        this.m_name = _name;
        this.m_activated = false;
    }

    public int getID() {
        return this.m_ID;
    }

    public String getName() {
        return this.m_name;
    }

    public boolean isActivated() {
        return this.m_activated;
    }

    public void setActivated(boolean _activated) {
        m_activated = _activated;
    }
}
