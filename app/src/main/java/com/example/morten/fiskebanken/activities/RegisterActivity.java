package com.example.morten.fiskebanken.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.example.morten.fiskebanken.utility.Fisk;
import com.example.morten.fiskebanken.R;
import com.example.morten.fiskebanken.database.FishDataSource;
import com.example.morten.fiskebanken.services.FiskeService;
import com.example.morten.fiskebanken.services.LocationFinder;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity{


    BroadcastReceiver getLocationFromBroadcast;
    static Bitmap photo;
    private static FishDataSource fishDataSource;
    private int fishNumber = 0;
    Button mButton;
    Button mButton1;
    EditText mEdit1;
    EditText mEdit2;
    EditText mEdit3;
    ImageView mImageView;
    LocationFinder mLocationFinder;
    Location mLoc;
   // private FiskeService fiskeService;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fishDataSource = new FishDataSource(this);
        try{
            fishDataSource.open();
        }
        catch (SQLException e){
            e.printStackTrace();
        }



        mButton = (Button)findViewById(R.id.registrerfisk);
        mButton1 = (Button) findViewById(R.id.tabilde);
        mEdit1 = (EditText)findViewById(R.id.fisketype);
        mEdit2 = (EditText)findViewById(R.id.fiskevekt);
        mEdit3 = (EditText)findViewById(R.id.fiskelengde);
        mImageView = (ImageView) findViewById(R.id.finnbilde);

        mButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Fisk fisk = fishDataSource.createFisk(mEdit1.getText().toString(),
                                Double.parseDouble(mEdit2.getText().toString()),
                                Double.parseDouble(mEdit3.getText().toString()),
                                mCurrentPhotoPath

                        );

                        sendImageToMap();


                        //System.out.println(mLocationFinder.GetPosition());
                        //   MapsActivity.addFishMarker(mLocationFinder.GetPosition(), "Fish");


                    }
                }
        );

        mButton1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dispatchTakePictureIntent();
                    }
                }
        );

        startService(new Intent(this, FiskeService.class));

    }
    public void setLocation(Location location){
       mLoc = location;
       MapsActivity.addFishMarker(mLoc);
    }

    private void sendImageToMap(){
        Intent i = new Intent("ForceLocationUpdate");
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {

        // Create an image file name

       String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
       String imageFileName = "JPEG_" + timeStamp + "_";
       File storageDir = Environment.getExternalStoragePublicDirectory(
               Environment.DIRECTORY_PICTURES);
       File image = File.createTempFile(
               imageFileName,  /* prefix */
               ".jpg",         /* suffix */
               storageDir      /* directory */
       );

       // Save a file: path for use with ACTION_VIEW intents
       mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /*public static byte[] getBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }*/


      /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
           photo = (Bitmap) data.getExtras().get("data");
            //mImageView.setImageBitmap(photo);
        }
    }*/
}