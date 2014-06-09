package com.CNAM.GeoRouting;
import java.util.Date;

/**
 * Created by guillaumekoehrlen on 14/05/2014.
 */
public class Position {

	private Date _currentDate;
    private double _latitude;
    private double _longitude;

    public Position()
    {
        this._currentDate=null;
        this._longitude = 0;
        this._latitude = 0;
    }

    public Position(double longitude, double latitude, Date dateRecup)
    {
        this._currentDate=dateRecup;
        this._latitude= latitude;
        this._longitude = longitude;
    }

    public Date get_currentDate() {
        return _currentDate;
    }

    public void set_currentDate(Date _currentDate) {
        this._currentDate = _currentDate;
    }

    public double get_latitude() {
        return _latitude;
    }

    public void set_latitude(double _latitude) {
        this._latitude = _latitude;
    }

    public double get_longitude() {
        return _longitude;
    }

    public void set_longitude(double _longitude) {
        this._longitude = _longitude;
    }
    
    @Override
    public String toString()
    {
    	return get_longitude() + "; " + get_latitude();
    }
}
