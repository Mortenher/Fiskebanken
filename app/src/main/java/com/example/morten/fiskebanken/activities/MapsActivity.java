package com.example.morten.fiskebanken.activities;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Property;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.morten.fiskebanken.database.FishDataSource;
import com.example.morten.fiskebanken.utility.Fisk;
import com.example.morten.fiskebanken.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        AdapterView.OnItemSelectedListener,
        GoogleMap.OnMapLongClickListener {

    static GoogleMap mMap;
    private static FishDataSource fishDataSource;
    static private ArrayList<Marker> mFishMarkers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        //Arrayliste som skal holde på markører
        mFishMarkers = new ArrayList<>();

        fishDataSource = new FishDataSource(this);
        try{
            fishDataSource.open();
        }
        catch (SQLException e){
            e.printStackTrace();
        }


        Spinner spinner = (Spinner)findViewById(R.id.layers_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.layers_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }


    //Setter innstillinger på kartet
    private void setUiSettings() {
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    //Sjekker om kartet er klart
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            setUiSettings();
            mMap.setOnMapLongClickListener(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
        //Hvis kartet er klart, legg til markører på kartet
        leggTilFisk();
    }

    //Endre utseende på kartet
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String layerType = (String)parent.getItemAtPosition(position);
        if(layerType.equals(getString(R.string.hybrid))){
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        else if(layerType.equals(getString(R.string.satellite))){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else if(layerType.equals(getString(R.string.terrain))){
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        else if(layerType.equals(getString(R.string.none))){
            mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        }
        else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }


    //Ordne bildestørrelse så de ikke skal dekke hele kartet
    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap b = BitmapFactory.decodeFile(iconName);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(b, width, height, false);
        return resizedBitmap;
    }

    //Legger til markører på kartet, henter location fra database
    private void leggTilFisk(){

        List<Fisk> fisker = fishDataSource.getAllFisk();

        for(Fisk f:fisker){


        double lat = f.getLat();
        double lon = f.getLng();
        LatLng tempLoc = new LatLng(lat,lon);
        String bilde = f.getBilde();

        Bitmap b = resizeMapIcons(bilde,100,100);
        BitmapDescriptor bm = BitmapDescriptorFactory.fromBitmap(b);


        //Markør som sender med ID slik at man kan bli sendt til Fishinfoactivity
        Marker mark =  mMap.addMarker(new MarkerOptions().position(tempLoc).icon(bm).snippet(f.getId()+""));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                visFisk(Integer.parseInt(marker.getSnippet()));
                return false;
            }
        });
            mFishMarkers.add(mark);

        }
    }

    private void visFisk(int position){
        Intent intent = new Intent(MapsActivity.this, FishinfoActivity.class);
        intent.putExtra("Id",position);
        startActivity(intent);
    }
}
