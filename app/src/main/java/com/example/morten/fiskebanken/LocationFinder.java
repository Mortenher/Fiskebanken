package com.example.morten.fiskebanken;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;


/**
 * Created by Morten on 06.04.2016.
 */
public class LocationFinder extends Application implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private FiskeService FiskeService;


   public LocationFinder(FiskeService fiskeService){
        FiskeService = fiskeService;
       System.out.println("DÆVEN, DA BLIR DET SPENNENDE Å SE OM DEN KOMMER HIT?");
        mGoogleApiClient = new GoogleApiClient.Builder(fiskeService)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                 .addApi(LocationServices.API)
                 .build();
         mGoogleApiClient.connect();
       System.out.println("Tror ikke den kommer hit da ");
    }



    public Location GetPosition() {
        try {
            if (ActivityCompat.checkSelfPermission(FiskeService, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FiskeService, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        if(mGoogleApiClient.isConnected()){
            return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        else{
            mGoogleApiClient.connect();
            return null;
        }
    }
    public void requestLocationUpdate() {

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setSmallestDisplacement(50);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //Checking if the app has sufficient permissions to request location
       try {
           if (ActivityCompat.checkSelfPermission(FiskeService, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FiskeService, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               return;
           }
       }
       catch(Exception e){
           e.printStackTrace();
       }
        //Requesting that play services trigger callback method "onLocationChanged" whenever location changes.
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    public void unRequestLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("Det burde bety at den kommer hit også!");
        requestLocationUpdate();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
      FiskeService.RequestApiCall(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Error", "Cant connect");
    }

}
