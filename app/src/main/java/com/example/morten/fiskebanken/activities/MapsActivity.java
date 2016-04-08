package com.example.morten.fiskebanken.activities;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.pm.PackageManager;
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

import com.example.morten.fiskebanken.utility.Fisk;
import com.example.morten.fiskebanken.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        AdapterView.OnItemSelectedListener,
        GoogleMap.OnMapLongClickListener {

    static GoogleMap mMap;

    private LatLng HIOF = new LatLng(59.12797849, 11.35272861);
   static private ArrayList<Marker> mFishMarkers;
   static private int mFishCounter = 0;
    Fisk fisk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        mFishMarkers = new ArrayList<>();

        Spinner spinner = (Spinner)findViewById(R.id.layers_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.layers_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }


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
        switch (id){
            case R.id.add_fisk:
                for (Marker fishMarker : mFishMarkers)
                    animateMarker(fishMarker, HIOF);
                break;
            case R.id.remove_fisk:
                removeAllFishMarkers();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void removeAllFishMarkers(){
        for (Marker fishMarker: mFishMarkers){
            fishMarker.remove();
        }
        mFishMarkers.clear();
        mFishCounter = 0;
    }

    static void animateMarker (Marker marker, LatLng finalPosition){
        TypeEvaluator<LatLng> typeEvaluator = new TypeEvaluator<LatLng>() {
            @Override
            public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
                double lat = (endValue.latitude - startValue.latitude) * fraction + startValue.latitude;
                double lng = (endValue.longitude - startValue.longitude) * fraction + startValue.longitude;
                return new LatLng(lat, lng);
            }
        };
        Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");
        ObjectAnimator animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, finalPosition);
        animator.setDuration(1000);
        animator.start();
    }

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
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(HIOF, 13, 0, 0)));
        }
    }

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
        //addFishMarker(latLng, "Fish image!");
    }

    static void addFishMarker(Location fishLocation){

        //BitmapDescriptor kittenIcon = BitmapDescriptorFactory.fromResource(
             //   getResources().getIdentifier("fish_background" + (mFishCounter % 3 + 1), "drawable", this.getPackageName()));

        //mFishCounter++;
       // LatLng tempLoc = new LatLng(fishLocation.getLatitude(),fishLocation.getLongitude());

       // MarkerOptions markerOptions = new MarkerOptions()
               // .position(new LatLng(fishLocation.getLatitude(), fishLocation.getLongitude()));

        double lat = fishLocation.getLatitude();
        double lon = fishLocation.getLongitude();
        LatLng tempLoc = new LatLng(lat,lon);


        Marker marker = mMap.addMarker(new MarkerOptions().position(tempLoc));
        mFishMarkers.add(marker);
      //  Log.d("Marker", kittenLocation.toString());

        //Marker fishMarker = mMap.addMarker(markerOptions);

    }
}
