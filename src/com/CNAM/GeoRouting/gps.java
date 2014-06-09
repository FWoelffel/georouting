package com.CNAM.GeoRouting;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import  android.location.*;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.Date;

/**
 * Created by guillaumekoehrlen on 07/05/2014.
 */
public class gps extends Service implements LocationListener, Igps{
	
	private static gps _instance = null;
    
	private final Context _mContext;

    protected boolean _isGPSEnabled = false;
    protected boolean _isNetworkEnabled = false;
    protected boolean _canGetLocation = false;
    protected Location _location; // location
    protected Position _position = null;
    protected LocationManager _locationManager;
    
    // Permet � la MainActivity d'acc�der � la m�thode permettant d'acc�der au service
    private IBinder _binder = new GPSModification();

    private Singleton_gps _singleton = Singleton_gps.getInstance();

    public gps()
    {
    	this._mContext = this;
    	_instance = this;
    }

    public static gps get_Instance(Context context)
    {
        if(_instance == null)
        {
        	_instance = new gps();
            /*_instance = new gps(context);*/
            _instance.initLocation();
        }
        return _instance;
    }

    /**
     * Anciennement GetLocation();
     * Initialise toutes les variables permettant l'utilisation du GPS
     * Temps de rafraichissement par d�faut : 5 minutes (300.000 ms)
     */
    public void initLocation() {
    try {
    	// Get access to hardware
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
            	// Bind onLocationUpdate event to get last location
                _locationManager.requestLocationUpdates(     LocationManager.NETWORK_PROVIDER,
                                                             GpsFactory.MIN_TIME_BW_UPDATES, // 300.000 ms
                                                             GpsFactory.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                Log.d("GPS.initLocation", "Network");
                if (_locationManager != null) {
                	// Get the last known location
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
                	// Bind onLocationUpdate event to get last location
                    _locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                                                             GpsFactory.MIN_TIME_BW_UPDATES,
                                                             GpsFactory.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("GPS.initLocation", "GPS Enabled");
                    if (_locationManager != null) {
                    	// Get the last known location
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
            _singleton.set_lastPosition(_position);

    } catch (Exception e) {
        e.printStackTrace();
    }
}
   
    /**
     * Actualise la position actuelle � partir de la "location" r�cup�r�e du GPS 
     */
    @Override
    public void updatePosition()
    {
        if(_location != null)
        {
            _position = new Position( _location.getLongitude(),
                                      _location.getLatitude(),
                                      new Date());
            _singleton.set_lastPosition(_position);
        }
        else
            _position=null;
    }

    @Override
    public boolean canGetLocation() {
        return this._canGetLocation;
    }
    
    @Override
    public void updateRefreshInterval(int newInterval){
    	if(_locationManager != null){
            _locationManager.removeUpdates(_instance);
        }
    	if (_isNetworkEnabled)
        {
        	// Bind onLocationUpdate event to get last location
            _locationManager.requestLocationUpdates(     LocationManager.NETWORK_PROVIDER,
                                                         newInterval * 1000,
                                                         0, this);
        }
        // if GPS Enabled get lat/long using GPS Services
        if (_isGPSEnabled)
        {
        	// Bind onLocationUpdate event to get last location
            _locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
            										 newInterval * 1000,
                                                     0, this);
        }
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
        return _singleton.estProcheHome(_position);
    }

    @Override
    public boolean is_movement() {
        return _singleton.is_estEnVoiture();
    }

    @Override
    public Position get_position()
    {
        return _position;
    }

    /**
     * Ev�nement appel� lors du rafraichissement par le GPS (et actualise la position)
     * @param la nouvelle location
     */
    @Override
    public void onLocationChanged(Location location) {
    	Log.d("GPS.onLocationChanged", "position changed : " + location.getLongitude() + "; " + location.getLatitude());
    	_location = location;
    	updatePosition();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return _binder;
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
    @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		stopUsingGPS();
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		initLocation();
		return super.onStartCommand(intent, flags, startId);
	}
	
	/**
	 * Classe cr�ant une m�thode pour retourner l'instance du GPS en cours
	 * @author EFAUST
	 *
	 */
	public class GPSModification extends Binder{
		/**
		 * Retourne l'instance actuelle du GPS
		 * @return instance du GPS
		 */
		gps GetService(){
			return gps.this;
		}
	}
}
