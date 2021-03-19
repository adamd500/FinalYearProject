package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.R;
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

public class SelectedCurrentJob extends AppCompatActivity {

    private ImageView imgView;
    private FirebaseDatabase database;
    private DatabaseReference ref, ref2;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseUser user;
    private String photoUrl;
    private String jobId, customerId;
     Job job;
    private TextView t1, t2, t3, t4, t5, t6, t7;
     Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_current_job);

        imgView = (ImageView) findViewById(R.id.imageView);

        storage = FirebaseStorage.getInstance();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref2 = database.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        jobId = intent.getStringExtra("id");
        customerId = intent.getStringExtra("customerId");

        t1 = (TextView) findViewById(R.id.txtViewTitle);
        t2 = (TextView) findViewById(R.id.customerName);
        t3 = (TextView) findViewById(R.id.customerPhone);
        t4 = (TextView) findViewById(R.id.jobLocation);
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
                        Intent intent = new Intent(SelectedCurrentJob.this, InboxProfessional.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(SelectedCurrentJob.this, ProfileHomePage.class);
                        startActivity(intent1);
                        return true;

                    case R.id.work:
                        Intent intent2 = new Intent(SelectedCurrentJob.this, WorkHomepage.class);
                        startActivity(intent2);
                        return true;

                    case R.id.stats:
                        Intent intent3 = new Intent(SelectedCurrentJob.this, ViewProfessionalFeedback.class);
                        intent3.putExtra("professionalId",uid);
                        startActivity(intent3);
                        return true;

                    case R.id.home:
                        Intent intent4 = new Intent(SelectedCurrentJob.this, WelcomeProfessional.class);
                        startActivity(intent4);
                }

                return false;
            }
        });
    }

    public void showOnMap(View v){
        String location = job.getLocation();

        Intent intent=new Intent(this,CurrentSelectedJobOnMap.class);
        intent.putExtra("location",location);
        startActivity(intent);

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

                        t1.setText("Job ID : " + job.getJobId());
                        t2.setText("Customer Name : " + customer.getName());
                        t3.setText("Customer Phone Number : " + customer.getPhoneNumber());
                        t4.setText("Job Location : " + job.getLocation());
                        t5.setText("Job Start Date : " + job.getStartDate());
                        t6.setText("Estimated Job Duration : " + job.getEstimatedDuration());
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
                                    Toast.makeText(SelectedCurrentJob.this, "Image Failed to Load", Toast.LENGTH_SHORT).show();
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


}

