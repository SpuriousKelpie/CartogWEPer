package com.kelpie.cartogweper;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class GPSService extends Service {

    LocationRequest location_request;
    long update_interval = 10 * 1000;
    long fastest_interval = 2000;
    final static String ACTION = "GPS ACTION";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        startLocationUpdates();
    }

    public void onLocationChanged(Location location) {
        String lat = Double.toString(location.getLatitude());
        String lon = Double.toString(location.getLongitude());
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.putExtra("lati", lat);
        intent.putExtra("long", lon);
        sendBroadcast(intent);
    }

    protected void startLocationUpdates() {
        // Create the location request to start receiving updates
        //location_request = new LocationRequest();
        location_request = LocationRequest.create();
        location_request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        location_request.setInterval(update_interval);
        location_request.setFastestInterval(fastest_interval);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(location_request);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getFusedLocationProviderClient(this).requestLocationUpdates(location_request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    onLocationChanged(locationResult.getLastLocation());
                }
            },
            Looper.myLooper());
        }
        else {
            return;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
