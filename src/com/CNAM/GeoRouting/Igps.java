package com.CNAM.GeoRouting;

/**
 * Interface GPS
 */
public interface Igps
{
    /**
     * Test if the current user is in move
     * @return boolean
     */
    public boolean is_movement();

    /**
     * Return the current position. Compose of the latitude and longitude
     * @return Position
     */
    public Position get_position();

    /**
     * Update the position of the user
     */
    public void updatePosition();

    /**
     * Test if the user can be located
     * @return boolean
     */
    public boolean canGetLocation();

    /**
     * Shutdown the GPS
     */
    public void stopUsingGPS();

    /**
     * Define the user's house
     * @param latitude
     * @param longitude
     */
    public void set_Home(double latitude,double longitude);

    /**
     * Test if the user is just next to his home
     * @return boolean
     */
    public boolean is_nearHome();
}
