package com.CNAM.GeoRouting;

public class Profile {

    private boolean m_activated = false;
    private final String m_description;
    private final String m_name;

    public Profile (String _name, String _description) throws ProfileException {
        if (_name.equals("") || _name.equals(null)) throw new ProfileException();
        m_name = _name;
        m_description = _description;
    }

    public boolean isActivated()
    {
        return m_activated;
    }

    public String getDescription()
    {
        return m_description;
    }

    public String getName()
    {
        return m_name;
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
        return "Profile :\n\tName : " + getName() + "\n\tDescription : " + getDescription() + "\n\tState : " + (isActivated() ? "Active" : "Inactive");
    }
}
