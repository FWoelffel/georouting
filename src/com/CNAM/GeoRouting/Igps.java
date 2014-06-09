package com.CNAM.GeoRouting;
/**
 * Created by guillaumekoehrlen on 07/05/2014.
 */
public interface Igps {

	public boolean is_movement();

    public Position get_position();

    public void updatePosition();

    public boolean canGetLocation();
    
    /**
     * Configure le GPs avec le nouvel interval de temps
     * @param newInterval le temps de rafraichissement en secondes
     */
    public void updateRefreshInterval(int newInterval);

    public void stopUsingGPS();

    public void set_Home(double latitude,double longitude);

    public boolean is_nearHome();
	
}
