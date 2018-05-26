package com.example.mahesh.alarmlocator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.mahesh.alarmlocator.model.Alarmmodel;
import com.google.android.gms.maps.model.LatLng;
import com.example.mahesh.alarmlocator.AddAlarmActivity.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyService extends Service {
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 100;
    private static final float LOCATION_DISTANCE = 0f;
    public Date current_date = null;
    public  Date  sdate , edate ;
    public static SimpleDateFormat di = new SimpleDateFormat("dd-MM-yy ");
    public String current_time ;
    public static SimpleDateFormat time = new SimpleDateFormat("HH:mm",Locale.ENGLISH);
    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            ArrayList<Alarmmodel> alarmList = new ArrayList<>();
            alarmList = Uttils.getAddressList(MyService.this);
            try {
                checkAlarm(alarmList, location);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    private void checkAlarm(ArrayList<Alarmmodel> alarmList, Location location) throws ParseException {

        Calendar c = Calendar.getInstance();

        Date de ;
         de = c.getTime();
        
        String cd = di.format(de);
        

       
        try {
             current_date = di.parse(cd);
            
        } catch (ParseException e) {
            e.printStackTrace();
        }

             current_time = String.valueOf(de.getHours()+":");
        if(de.getMinutes()<10) {
        current_time = current_time +"0"+String.valueOf(de.getMinutes());
        }
        else
            current_time = current_time+String.valueOf(de.getMinutes());

        



        if (alarmList != null) {
            for (Alarmmodel alarmmodel : alarmList) {

                if(!alarmmodel.isDisable()) {
                    if (checkDistance(location, alarmmodel)) {


                        sdate = alarmmodel.getSdate();
                        edate = alarmmodel.getEdate();

                        if((current_date.after(sdate) && current_date.before(edate) )|| current_date.equals(sdate) || current_date.equals(edate))
                        {
                            int m = current_time.compareTo(alarmmodel.getEtime());
                            int n = current_time.compareTo(alarmmodel.getStime());


                            if((m<0 && n>0) || m==0 || n==0)
                                MyService.this.startActivity(getPopupIntent(alarmmodel));
                         }
                    }
                        }

                else {
                        continue;
                    }
                }
            }
        }

    private boolean checkDistance(Location location, Alarmmodel alarmmodel) {

        Location loc2 = new Location("");
        loc2.setLatitude(Double.valueOf(alarmmodel.getLat()));
        loc2.setLongitude(Double.valueOf(alarmmodel.getLng()));
        float distanceInMeters = location.distanceTo(loc2);
        
        if(distanceInMeters<=100){
            return true;
        }
        return false;
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    public Intent getPopupIntent(Alarmmodel alarmmodel) {
        Intent popup = new Intent(MyService.this, PopUpActivity.class);
        popup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.ALARM_MODEl, alarmmodel);
        popup.putExtras(bundle);
        return popup;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
             Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}