package com.example.morten.fiskebanken.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.morten.fiskebanken.utility.Fisk;
import com.example.morten.fiskebanken.R;
import com.example.morten.fiskebanken.database.FishDataSource;
import com.example.morten.fiskebanken.services.FiskeService;
import com.example.morten.fiskebanken.services.LocationFinder;
import com.example.morten.fiskebanken.utility.NotificationReceiver;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity{


    private static FishDataSource fishDataSource;
    Button mButton;
    Button mButton1;
    EditText mEdit1;
    EditText mEdit2;
    EditText mEdit3;
    TextView mEdit4;
    ImageView mImageView;
    LocationFinder mLocationFinder;
    Location mLoc;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Finner gps-lokasjon din for senere bruk
        mLocationFinder = new LocationFinder(this);
        mLoc = mLocationFinder.GetPosition();

        //Åpner en kobling til SQLite databasen
        fishDataSource = new FishDataSource(this);
        try{
            fishDataSource.open();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        //Notification intent, skal vises om man ikke har brukt appen på en uke
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        //Tid + en uke
        am.set(am.RTC_WAKEUP, System.currentTimeMillis() + 604800000, pendingIntent);

        mButton = (Button)findViewById(R.id.registrerfisk);
        mButton1 = (Button) findViewById(R.id.tabilde);
        mEdit1 = (EditText)findViewById(R.id.fisketype);
        mEdit2 = (EditText)findViewById(R.id.fiskevekt);
        mEdit3 = (EditText)findViewById(R.id.fiskelengde);
        mEdit4 = (TextView)findViewById(R.id.errormessage);
        mImageView = (ImageView) findViewById(R.id.finnbilde);

        mButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Diverse tester for å sjekke om alt er på plass
                        if(mCurrentPhotoPath == null){
                            mEdit4.setText("Du må ta et bilde!");
                        }
                        else {
                            //Sjekker igjen om location eksisterer, om ikke. Finn posisjon på nytt
                            mLoc = mLocationFinder.GetPosition();
                            if(mLoc == null){
                                mEdit4.setText("Finner ikke din posisjon, kan ikke registrerers");
                            }
                            //Lager et fiskeobjekt i databasen
                            Fisk fisk = fishDataSource.createFisk(mEdit1.getText().toString(),
                                    Double.parseDouble(mEdit2.getText().toString()),
                                    Double.parseDouble(mEdit3.getText().toString()),
                                    mCurrentPhotoPath,
                                    mLoc.getLatitude(),
                                    mLoc.getLongitude()
                            );
                            //Gå ut av aktiviteten og tilbake til main
                            try {
                                exitActivity();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
        //Start kamera-intent
        mButton1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dispatchTakePictureIntent();
                    }
                }
        );

        //Er denne overflødig?
        startService(new Intent(this, FiskeService.class));

    }
    //Kan ikke bruke .finish() i onclick, så lager en egen metode
    private void exitActivity(){
        this.finish();
    }

   /* public void setLocation(Location location){
       mLoc = location;
       MapsActivity.addFishMarker(mLoc);
    }*/

    /*private void sendImageToMap(){
        Intent i = new Intent("ForceLocationUpdate");
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }*/

    String mCurrentPhotoPath;
    //Lager en bildefil som returnerer path for lagring i databasen
    private File createImageFile() throws IOException {

        // Lager filnavn

       String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
       String imageFileName = "JPEG_" + timeStamp + "_";
       File storageDir = Environment.getExternalStoragePublicDirectory(
               Environment.DIRECTORY_PICTURES);
       File image = File.createTempFile(
               imageFileName,
               ".jpg",
               storageDir
       );

       // Returnerer fil og filsti
       mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    //Metoden som starter kamera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //Sjekker at filen ikke er tom
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


}