package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.ObjectClasses.Consultation;
import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.ObjectClasses.Job;
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
import java.util.Calendar;
import java.util.List;

public class MapCurrentJob extends FragmentActivity implements OnMapReadyCallback {
    private ImageView imgView;
    private FirebaseDatabase database;
    private DatabaseReference ref, ref2;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseUser user;
    private String photoUrl;
    private String jobId, customerId;
    Job job;
    private TextView t1, t2, t3, t5, t6, t7;
    private GoogleMap mMap;
    double latitude,longitude;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_current_job);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        imgView = (ImageView) findViewById(R.id.imageView);

        storage = FirebaseStorage.getInstance();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref2 = database.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        jobId = intent.getStringExtra("id");
        customerId = intent.getStringExtra("customerId");
         location=intent.getStringExtra("location");

        t1 = (TextView) findViewById(R.id.txtViewTitle);
        t2 = (TextView) findViewById(R.id.customerName);
        t3 = (TextView) findViewById(R.id.customerPhone);
        t5 = (TextView) findViewById(R.id.startDate);
        t6 = (TextView) findViewById(R.id.quotedDuration);
        t7 = (TextView) findViewById(R.id.quotedPrice);
        imgView = (ImageView) findViewById(R.id.imageView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        getCustomer();
        //getJob();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(MapCurrentJob.this, InboxProfessional.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(MapCurrentJob.this, ProfileHomePage.class);
                        startActivity(intent1);
                        return true;

                    case R.id.work:
                        Intent intent2 = new Intent(MapCurrentJob.this, WorkHomepage.class);
                        startActivity(intent2);
                        return true;

                    case R.id.stats:
                        Intent intent3 = new Intent(MapCurrentJob.this, ViewProfessionalFeedback.class);
                        intent3.putExtra("professionalId",uid);
                        startActivity(intent3);
                        return true;

                    case R.id.home:
                        Intent intent4 = new Intent(MapCurrentJob.this, WelcomeProfessional.class);
                        startActivity(intent4);
                }

                return false;
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        Geocoder coder = new Geocoder(MapCurrentJob.this);

                try {
                    List<Address> locations=coder.getFromLocationName(location,2);

                    if(locations!=null){
//                        latitude =locations.get(0).getLatitude();
//                        longitude=locations.get(0).getLongitude();
                        latitude =53.349081;
                        longitude= -6.431490;

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        LatLng selectedJob = new LatLng(latitude,longitude);
       // LatLng selectedJob = new LatLng( 53.357283,-6.370727);

        mMap.addMarker(new MarkerOptions().position(selectedJob).title("Job Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedJob));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedJob,11.0f));
    }

    public void showOnMap(View v){
        ref.child("Job").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(jobId)) {
                        job = child.getValue(Job.class);

                        Uri uri=Uri.parse("google.navigation:q="+job.getLocation());
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        intent.setPackage("com.google.android.apps.maps");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    public void getCustomer() {
        ref2.child("Customer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(customerId)) {
                        Customer customer = child.getValue(Customer.class);
                        getJob(customer);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }


    public void getJob(Customer customer) {

        ref.child("Job").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(jobId)) {
                        job = child.getValue(Job.class);


                        photoUrl = job.getListing().getPhotos().get(0);

                        t1.setText("Current Job \n  " + job.getJobTitle());
                        t2.setText("Name : " + customer.getName());
                        t3.setText("Phone Number : " + customer.getPhoneNumber());
                        t5.setText("Start Date : " + job.getStartDate());
                        t6.setText("Estimated Duration : " + job.getEstimatedDuration());
                        t7.setText("Quoted Price : " + job.getQuote());

                        storageReference = storage.getReferenceFromUrl("gs://fypdatabase-d9dfe.appspot.com"+photoUrl);

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
                                    Toast.makeText(MapCurrentJob.this, "Image Failed to Load", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
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

    public void completeJob(View v){

        Intent intent = new Intent(this,FinaliseJobProfessional.class);
        intent.putExtra("id",job.getJobId());
        startActivity(intent);

    }

    public void addToCalendar(View view) {

        ref.child("Job").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(jobId)) {
                        job = child.getValue(Job.class);

                        String[] date = job.getStartDate().split("/");
                        int day = Integer.parseInt(date[0]);
                        int month = Integer.parseInt(date[1]);
                        int year = Integer.parseInt(date[2]);

                        Calendar beginCal = Calendar.getInstance();
                        beginCal.set(year, month, day);

                        Calendar finishCal = Calendar.getInstance();
                        finishCal.set(year, month, day);
                        finishCal.add(Calendar.DAY_OF_YEAR, Integer.parseInt(job.getEstimatedDuration()));

                        Intent insertCalendarIntent = new Intent(Intent.ACTION_INSERT).setData(CalendarContract.Events.CONTENT_URI)
                                .putExtra(CalendarContract.Events.TITLE, "Working on Job Titled :" + job.getJobTitle())
                                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginCal.getTimeInMillis())
                                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, finishCal.getTimeInMillis())
                                .putExtra(CalendarContract.Events.EVENT_LOCATION, job.getLocation())
                              //  .putExtra(Intent.EXTRA_EMAIL, customerEmail)
                                .putExtra(CalendarContract.Events.DESCRIPTION, "Description :" + job.getDescription()) // Description
                                .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE)
                                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);

                        startActivity(insertCalendarIntent);

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