package com.CNAM.GeoRouting;

/**
 * Created by guillaumekoehrlen on 14/05/2014.
 */
public class Singleton_gps
{
    private static Singleton_gps _instance;

    private Position _home = null;
    private Position _lastPostion = null;
    private boolean _estEnVoiture = false;


    private Singleton_gps()
    {}

    public static Singleton_gps getInstance()
    {
        if(_instance==null)
            _instance= new Singleton_gps();
        return _instance;
    }

    public boolean is_estEnVoiture() {
        return _estEnVoiture;
    }

    public Position get_lastPostion() {
        return _lastPostion;
    }

    public void set_lastPostion(Position lastPostion)
    {
        if(this._lastPostion != null)   //déjà une position d'enregistré
        {
            double nbMiliSeconde = lastPostion.get_currentDate().getTime()- this._lastPostion.get_currentDate().getTime();
            double difHeure = nbMiliSeconde/(double)(1000*60*60);

            double lat1 = Math.toRadians(_lastPostion.get_latitude());
            double lat2 = Math.toRadians(lastPostion.get_latitude());
            double lon1 = Math.toRadians(_lastPostion.get_longitude());
            double lon2 = Math.toRadians(lastPostion.get_longitude());

            //sortie en km
            double distance = distanceKM(lat1, lon1, lat2, lon2);

            int kmParHeure = 0;

            if(difHeure >= 1)
                kmParHeure = (int)Math.round(distance/difHeure);
            else
            {
                double nbmin = difHeure*60;
                double coef = (double)60/nbmin;
                kmParHeure = (int) Math.round(coef * distance);
            }

            if (kmParHeure >= GpsFactory._vitesseMini && kmParHeure <= GpsFactory._vitesseMax)
                _estEnVoiture = true;
            else
                _estEnVoiture = false;
        }
        else
            this._estEnVoiture = false;

        this._lastPostion = lastPostion;
    }

    public double distanceKM(double lat1, double lon1, double lat2, double lon2)
    {
        double dp= 2 * Math.asin(Math.sqrt(Math.pow((Math.sin((lat1 - lat2) / 2)),2)+Math.cos(lat1) * Math.cos(lat2) *(Math.pow( Math.sin(((lon1-lon2)/2)), 2))));
        return dp* GpsFactory._rayonTerrestre;
    }

    public boolean estProcheHome(Position posActu)
    {
        if(_home == null)
            return false;


        double distance = distanceKM(posActu.get_latitude(),posActu.get_longitude(),_home.get_latitude(),_home.get_longitude());

        if(distance <= GpsFactory._distanceMaisonMax)
            return true;
        else
            return false;
    }

    public void set_home(Position _home)
    {
        this._home = _home;
    }
}
