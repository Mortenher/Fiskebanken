package com.example.morten.fiskebanken.services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.example.morten.fiskebanken.activities.RegisterActivity;

/**
 * Created by Morten on 06.04.2016.
 */
public class FiskeService extends IntentService{

   LocationFinder locationFinder;
   BroadcastReceiver forceUpdateReciever;
    static RegisterActivity ra = new RegisterActivity();

   public FiskeService(String name){
       super(name);
   }
    public FiskeService(){
        super("NewThread");
    }
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    protected void onHandleIntent(Intent intent) {

    }
    @Override
    public void onCreate(){

        locationFinder = new LocationFinder(this);

        forceUpdateReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("Recieved");
                Location lastKnownLocation = locationFinder.GetPosition();
                if (lastKnownLocation != null) {
                    System.out.println("Location found, updating");
                    ra.setLocation(lastKnownLocation);
                    Runnable r = new RequestApiThread(lastKnownLocation);
                    new Thread(r).start();
                }
                else{
                    System.out.println("Location cant be found");
                }

            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(forceUpdateReciever, new IntentFilter("ForceLocationUpdate"));
    }

    public void sendToRegister(Location loc){
        Intent i = new Intent("getLocationFromBroadcast");
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);


    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return START_STICKY;
    }

    private void sendLocationToActivity(){

    }

    public void RequestApiCall(Location location){


    }
    public class RequestApiThread implements Runnable{
        Location location;
        public RequestApiThread(Location location){
            this.location = location;
        }
        public void run(){
            if(location == null){
                locationFinder.requestLocationUpdate();
            }
            else{
                return;
            }
        }
    }
}
