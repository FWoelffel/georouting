package com.CNAM.GeoRouting;

import android.content.Context;

/**
 * Created by guillaumekoehrlen on 15/05/2014.
 */
public class GpsFactory
{
    private static GpsFactory _instance=null;

    public static int _vitesseMini = 15;
    public static int _vitesseMax = 200;
    public static double _rayonTerrestre = 6374.8925;
    public static double _distanceMaisonMax = 2;
    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    public static final long MIN_TIME_BW_UPDATES = (1000 * 60 * 1)/2; // 5 minutes


    private Context _context=null;

    private GpsFactory(Context context)
    {
        _context=context;
    }

    public static GpsFactory get_GpsFactory(Context context)
    {
        if(_instance == null)
            _instance = new GpsFactory(context);
        return _instance;
    }

    public Igps get_Interface()
    {
        gps leGps = gps.get_Instance(_context);
        Igps InterfaceGps = leGps;
        return InterfaceGps;
    }

}
