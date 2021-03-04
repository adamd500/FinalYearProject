package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;

public class CurrentSelectedJobOnMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String location;
    double longitude;
    double latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_selected_job_on_map);

        Intent intent = getIntent();
        location=intent.getStringExtra("location");

        Geocoder coder = new Geocoder(this);

        try {
            List<Address> locations=coder.getFromLocationName(location,2);

            if(locations!=null){
                 latitude =locations.get(0).getLatitude();
                 longitude=locations.get(0).getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng selectedJob = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(selectedJob).title("Current Job"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedJob));
    }
}