package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.ObjectClasses.Job;
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

public class SelectedFinishedJob extends AppCompatActivity {

    private ImageView imgView,img2,img3;
    private FirebaseDatabase database;
    private DatabaseReference ref, ref2;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseUser user;
    private String photoUrl;
    private String jobId, customerId;
    Job job;
    private TextView t1, t2, t3, t4, t5, t6, t7,t8,t9,t10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_finished_job);

        imgView = (ImageView) findViewById(R.id.imageView);
        img2 = (ImageView) findViewById(R.id.imageView2);
        img3 = (ImageView) findViewById(R.id.imageView3);

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
        t4 = (TextView) findViewById(R.id.jobLocation);
        t5 = (TextView) findViewById(R.id.startDate);
        t6 = (TextView) findViewById(R.id.endDate);

        t7 = (TextView) findViewById(R.id.quotedDuration);
        t8 = (TextView) findViewById(R.id.actualDuration);

        t9 = (TextView) findViewById(R.id.quotedPrice);
        t10 = (TextView) findViewById(R.id.actualPrice);

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

                        t1.setText( job.getJobTitle());
                        t2.setText("Name : " + customer.getName());
                        t4.setText("Location : " + job.getLocation());
                        t5.setText("Start Date : " + job.getStartDate());
                        t6.setText("End Date : " + job.getEndDate());
                        t7.setText("Estimated Duration : " + job.getEstimatedDuration());
                        t8.setText("Actual Duration : " + job.getActualDuration());
                        t9.setText("Quoted Price : " + job.getQuote());
                        t10.setText("Actual Price : " + job.getQuote());


                        storageReference = storage.getReferenceFromUrl("gs://fypdatabase-d9dfe.appspot.com" + photoUrl);

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

                        storageReference = storage.getReferenceFromUrl("gs://fypdatabase-d9dfe.appspot.com" + job.getAfterPics().get(0));

                        try {
                            final File file = File.createTempFile("image", "jpeg");
                            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                    img2.setImageBitmap(bitmap);
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

                        storageReference = storage.getReferenceFromUrl("gs://fypdatabase-d9dfe.appspot.com" + job.getAfterPics().get(1));


                        try {
                            final File file = File.createTempFile("image", "jpeg");
                            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                    img3.setImageBitmap(bitmap);
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