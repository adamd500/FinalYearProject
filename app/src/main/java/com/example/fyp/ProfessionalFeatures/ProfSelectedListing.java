package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.Messaging.StartConversation2;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProfSelectedListing extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView t1, t2, t3, t4;
    private ImageView imgView;

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseUser user;
    private String uid;
    private String listingId;
    private Listing listing;
    private String photoUrl;
    private Button button;
    private String title,location;
    double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_listing2);

        t2 = (TextView) findViewById(R.id.txtViewTrade);
        t3 = (TextView) findViewById(R.id.textViewDescription);
        t4 = (TextView) findViewById(R.id.txtViewTitle);
        //  button = (Button) findViewById(R.id.consultationButton);
        imgView = (ImageView) findViewById(R.id.imageView);

        storage = FirebaseStorage.getInstance();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        Intent i = getIntent();
        listingId = i.getStringExtra("id");
        title = i.getStringExtra("title");
        location=i.getStringExtra("location");

        t4.setText("Listing Title :\n " + title);
        //   t1.setText(id);
        getListing();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(ProfSelectedListing.this, InboxProfessional.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(ProfSelectedListing.this, ProfileHomePage.class);
                        startActivity(intent1);
                        return true;

                    case R.id.work:
                        Intent intent2 = new Intent(ProfSelectedListing.this, WorkHomepage.class);
                        startActivity(intent2);
                        return true;

                    case R.id.stats:
                        Intent intent3 = new Intent(ProfSelectedListing.this, ViewProfessionalFeedback.class);
                        intent3.putExtra("professionalId",uid);
                        startActivity(intent3);
                        return true;

                    case R.id.home:
                        Intent intent4 = new Intent(ProfSelectedListing.this, WelcomeProfessional.class);
                        startActivity(intent4);
                }

                return false;
            }
        });

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

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setTrafficEnabled(true);

        Geocoder coder = new Geocoder(ProfSelectedListing.this);

        try {
            List<Address> locations=coder.getFromLocationName(location,2);

            if(locations!=null){
                latitude =locations.get(0).getLatitude();
                longitude=locations.get(0).getLongitude();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        LatLng selectedJob = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(selectedJob).title("Listing Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedJob));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedJob,11.0f));
    }

    public void getListing() {
        ref.child("Listing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(listingId)) {
                        listing = child.getValue(Listing.class);
                        photoUrl = listing.getPhotos().get(0);
                        displayListing();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }


    @SuppressLint("SetTextI18n")
    public void displayListing() {
        t2.setText("Trade Sector : " + listing.getTradeSector());
        t3.setText("Description : " + listing.getDescription());

        storageReference = storage.getReferenceFromUrl("gs://fypdatabase-d9dfe.appspot.com" + photoUrl);

//fypdatabase-d9dfe.appspot.com/images
        try {
            final File file = File.createTempFile("image", "jpeg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    imgView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfSelectedListing.this, "Image Failed to Load", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        // imgView.setImageBitmap();
    }

    public void sendMessage(View v) {
        ref.child("Listing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(listingId)) {
                        Listing listing = child.getValue(Listing.class);

                        Intent intent = new Intent(ProfSelectedListing.this, StartConversation2.class);
                        intent.putExtra("listingId", listingId);
                        intent.putExtra("customerId", listing.getCustomerUsername());
                        intent.putExtra("listingTitle", listing.getTitle());


                        startActivity(intent);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

}