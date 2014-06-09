package com.CNAM.GeoRouting;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import  android.location.*;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import java.util.Date;


/**
 * Created by guillaumekoehrlen on 07/05/2014.
 */
public class gps extends Service implements LocationListener,Igps
{
    private static gps _instance = null;

    private final Context _mContext;

    protected boolean _isGPSEnabled = false;
    protected boolean _isNetworkEnabled = false;
    protected boolean _canGetLocation = false;
    protected Location _location; // location
    protected Position _position = null;
    protected LocationManager _locationManager;

    private Singleton_gps _singleton = Singleton_gps.getInstance();


    private gps(Context context)
    {
        this._mContext = context;
    }

    public static gps get_Instance(Context context)
    {
        if(_instance == null)
        {
            _instance = new gps(context);
            _instance.getLocation();
        }
        return _instance;
    }

    /**
     * First test to launch the GPS
     * @return
     */
    public Location getLocation() {
    try {
        _locationManager = (LocationManager) _mContext.getSystemService(LOCATION_SERVICE);

        // getting GPS status
        _isGPSEnabled = _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        _isNetworkEnabled = _locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!_isGPSEnabled && !_isNetworkEnabled)
        {
            // no network provider is enabled
        } else
        {
            this._canGetLocation = true;
            // First get location from Network Provider
            if (_isNetworkEnabled)
            {
                _locationManager.requestLocationUpdates(     LocationManager.NETWORK_PROVIDER,
                                                             GpsFactory.MIN_TIME_BW_UPDATES,
                                                             GpsFactory.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                Log.d("Network", "Network");
                if (_locationManager != null) {
                    _location = _locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(_location != null)
                    {
                        _position = new Position( _location.getLongitude(),
                                _location.getLatitude(),
                                new Date());
                    }
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (_isGPSEnabled)
            {
                if (_location == null) {
                    _locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                                                             GpsFactory.MIN_TIME_BW_UPDATES,
                                                             GpsFactory.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (_locationManager != null) {
                        _location = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(_location != null)
                        {
                            _position = new Position( _location.getLongitude(),
                                    _location.getLatitude(),
                                    new Date());
                        }
                    }
                }
            }
        }
        if(_position != null)
            _singleton.set_lastPostion(_position);

    } catch (Exception e) {
        e.printStackTrace();
    }

    return _location;
}

    /**
     * Update the current position
     */
    public void position()
    {
        if (_isGPSEnabled)
        {
            _location = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        else if(_isNetworkEnabled)
        {
            _location = _locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        else
            _location = null;

        if(_location != null)
        {
            _position = new Position( _location.getLongitude(),
                                      _location.getLatitude(),
                                      new Date());
            _singleton.set_lastPostion(_position);
        }
        else
            _position=null;
    }

    public double getLatitude()
    {
        position();
        return _position.get_latitude();
    }

    public double getLongitude()
    {
        position();
        return _position.get_longitude();
    }

    @Override
    public boolean canGetLocation() {
        return this._canGetLocation;
    }

    @Override
    public void stopUsingGPS(){
        if(_locationManager != null){
            _locationManager.removeUpdates(_instance);
        }
    }

    @Override
    public void set_Home(double latitude, double longitude) {
        _singleton.set_home(new Position(longitude,latitude,null));
    }

    @Override
    public boolean is_nearHome()
    {
        position();
        return _singleton.estProcheHome(_position);
    }

    @Override
    public boolean is_movement() {
        return _singleton.is_estEnVoiture();
    }

    @Override
    public Position get_position()
    {
        if(_position == null)
            position();
        return _position;
    }

    @Override
    public void updatePosition() {
        position();
    }

    @Override
    public void onLocationChanged(Location location) {

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }
    @Override
    public void onProviderEnabled(String s) {
    }
    @Override
    public void onProviderDisabled(String s) {
    }
}

