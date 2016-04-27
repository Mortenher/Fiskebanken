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

        //Setter opp en locationfinder
        locationFinder = new LocationFinder(this);
        //Setter location om funnet, starter ny tråd som stadig oppdaterer location
        forceUpdateReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("Recieved");
                Location lastKnownLocation = locationFinder.GetPosition();
                if (lastKnownLocation != null) {
                    System.out.println("Location found, updating");
                    Runnable r = new RequestLocationThread(lastKnownLocation);
                    new Thread(r).start();
                }
                else{
                    System.out.println("Location cant be found");
                }

            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(forceUpdateReciever, new IntentFilter("ForceLocationUpdate"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return START_STICKY;
    }
    //Metoden som tar for seg oppdatering av location på ny tråd
    public class RequestLocationThread implements Runnable{
        Location location;
        public RequestLocationThread(Location location){
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
