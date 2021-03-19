package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.ObjectClasses.Professional;
import com.example.fyp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BrowseJobsOnMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseUser user;
    private String uid;
    private FirebaseDatabase database;
    private DatabaseReference ref, ref2;
    HashMap<String, LatLng> idEircode = new HashMap<String, LatLng>();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_jobs_on_map);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref2 = database.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

       // populateHashMap();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (mMap != null) {
            int permission = ContextCompat.checkSelfPermission(BrowseJobsOnMap.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);

            if (permission == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }
    }

//    public LatLng getLocationFromEircode(String eircode) {
//        Geocoder coder = new Geocoder(this);
//        LatLng p1 = null;
//        try {
//
//            List<Address>  locations = coder.getFromLocationName(eircode, 2);
//            if (locations != null) {
//
//                double latitude = locations.get(0).getLatitude();
//                double longitude = locations.get(0).getLongitude();
////
//                p1 = new LatLng(latitude, longitude);
//
//            }else{
//                Toast.makeText(this, "No address found", Toast.LENGTH_SHORT).show();
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return p1;
//    }

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

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                String markerTitle = marker.getTitle();
                Intent i = new Intent(BrowseJobsOnMap.this, SelectedNewJob.class);
                i.putExtra("id", markerTitle);
                startActivity(i);

                return false;
            }
        });
        getProfLocation();

    }

    public void getProfLocation() {
        ref.child("Professional").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(uid)) {
                        Professional professional = child.getValue(Professional.class);
                        Geocoder coder = new Geocoder(getApplicationContext());
                        // LatLng p1 = null;
                        try {

                            List<Address>  locations = coder.getFromLocationName(professional.getLocation(), 2);
                            if (locations != null) {

                                double latitude = locations.get(0).getLatitude();
                                double longitude = locations.get(0).getLongitude();
//
                                LatLng  p1 = new LatLng(latitude, longitude);

                                mMap.addMarker(new MarkerOptions().position(p1).title("Your Location"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(p1));
                                getListingLocations(professional.getWorkRadius(),professional.getTrade());
                                mMap.getUiSettings().setZoomControlsEnabled(true);

                            }else{
                                Toast.makeText(getApplicationContext(), "No address found", Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        //getHashMapValues();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getListingLocations(int radius, String trade) {

        ref.child("Listing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Listing listing = child.getValue(Listing.class);
                    //  if (listing.getTradeSector().equals(trade)) {
                    Geocoder coder = new Geocoder(getApplicationContext());
                    LatLng p1 = null;
                    try {

                        List<Address>  locations = coder.getFromLocationName(listing.getLocation(), 2);
                        if (locations != null) {

                            double latitude = locations.get(0).getLatitude();
                            double longitude = locations.get(0).getLongitude();
//
                            p1 = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(p1).title(listing.getListingId()))
                                    .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p1, 12));

                        }else{
                            Toast.makeText(getApplicationContext(), "No address found", Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                   // LatLng myLocation = getLocationFromEircode(listing.getLocation());


                    // getListingLocations(professional.getWorkRadius(), professional.getTrade());

                    //   }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

//    public void populateHashMap() {
//        ref.child("Listing").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Iterable<DataSnapshot> children = snapshot.getChildren();
//                for (DataSnapshot child : children) {
//                    Listing listing = child.getValue(Listing.class);
//                    //   if (listing.getTradeSector().equals(trade)) {
//
//                    LatLng listingLoc = getLocationFromEircode(listing.getLocation());
//                    idEircode.put(listing.getListingId(), listingLoc);
//                    // getListingLocations(professional.getWorkRadius(), professional.getTrade());
//
//                    //  }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                //   Log.m("DBE Error","Cancel Access DB");
//            }
//        });
//    }

//    public void getHashMapValues() {
//        Iterator it = idEircode.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry) it.next();
//            String id = (String) pair.getKey();
//            LatLng loc = (LatLng) pair.getValue();
//            mMap.addMarker(new MarkerOptions().position(loc).title(id))
//                    .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 12));
//        }
//    }

}