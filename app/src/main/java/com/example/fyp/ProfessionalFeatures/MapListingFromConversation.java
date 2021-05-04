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
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.ObjectClasses.Professional;
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

public class MapListingFromConversation extends FragmentActivity implements OnMapReadyCallback {

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
    String location;
    private String title;
    double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_listing_from_conversation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        t1 = (TextView) findViewById(R.id.txtViewEircode);
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

        t4.setText("Listing :\n " + title);
        //   t1.setText(id);
        getListing();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(getApplicationContext(), InboxProfessional.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(getApplicationContext(), ProfileHomePage.class);
                        startActivity(intent1);
                        return true;

                    case R.id.work:
                        Intent intent2 = new Intent(getApplicationContext(), WorkHomepage.class);
                        startActivity(intent2);
                        return true;

                    case R.id.stats:
                        Intent intent3 = new Intent(getApplicationContext(), ViewProfessionalFeedback.class);
                        intent3.putExtra("professionalId",uid);
                        startActivity(intent3);
                        return true;

                    case R.id.home:
                        Intent intent4 = new Intent(getApplicationContext(), WelcomeProfessional.class);
                        startActivity(intent4);
                }

                return false;
            }
        });
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
        t1.setText("Listing location : " + listing.getLocation());
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
                    Toast.makeText(getApplicationContext(), "Image Failed to Load", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        // imgView.setImageBitmap();
    }

    public void makeConsultation(View v) {
        ref.child("Professional").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Professional professional = child.getValue(Professional.class);
                    if (professional.getUsername().equals(uid)) {


                        if ((professional.isIdVerified()) && (professional.isGardaVetVer()) && (professional.isSafePassVer())) {

                            Intent intent = new Intent(getApplicationContext(), ArrangeConsultation.class);
                            intent.putExtra("listingId", listingId);
                            startActivity(intent);

                        } else {

                            Toast.makeText(getApplicationContext(), "Error ! You cannot perform that action until all documents have been verified.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }

        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setTrafficEnabled(true);

        Geocoder coder = new Geocoder(MapListingFromConversation.this);

        try {
            List<Address> locations=coder.getFromLocationName(location,2);

            if(locations!=null){
                //     latitude =locations.get(0).getLatitude();
                //   longitude=locations.get(0).getLongitude();
                latitude =53.349081;
                longitude= -6.431490;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        LatLng selectedJob = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(selectedJob).title("Listing Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedJob));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedJob,11.0f));
    }

}