package com.CNAM.GeoRouting;

public class Profile {

    private boolean m_activated = false;
    private final int m_id;
    private final String m_name;

    public Profile (int _id, String _name) throws ProfileException {
        if (_id == -1) throw new ProfileException();
        m_name = _name;
        m_id = _id;
    }

    public boolean isActivated()
    {
        return m_activated;
    }

    public String getName()
    {
        return m_name;
    }

    public int getId() {
        return m_id;
    }

    public void setActivated(boolean _activated) {
        m_activated = _activated;
    }

    public boolean equals(Object o) {
        if(o instanceof Profile && ((Profile) o).getName().equals(m_name))
            return true;
        return false;
    }

    public String toString() {
        return "Profile :\n\tName : " + getName() + "\n\tState : " + (isActivated() ? "Active" : "Inactive");
    }
}
