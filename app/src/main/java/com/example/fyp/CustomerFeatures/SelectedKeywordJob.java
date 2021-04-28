package com.example.fyp.CustomerFeatures;

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

import com.example.fyp.Messaging.InboxCustomer;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class SelectedKeywordJob extends AppCompatActivity {

    TextView t1, t2, t3,t4;
    String jobId;
    ImageView imageView,img1, img2;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_keyword_job);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        Intent intent = getIntent();
        jobId=intent.getStringExtra("id");

        storage= FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        img1 = (ImageView) findViewById(R.id.imageView2);
        img2 = (ImageView) findViewById(R.id.imageView3);
        imageView = (ImageView) findViewById(R.id.imageView);

        t1=(TextView)findViewById(R.id.textViewDescription);
        t2=(TextView)findViewById(R.id.txtViewTrade);
        t3=(TextView)findViewById(R.id.txtViewEircode);
        t4=(TextView)findViewById(R.id.txtViewTitle);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(getApplicationContext(), InboxCustomer.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(getApplicationContext(), CustomerProfile.class);
                        startActivity(intent1);
                        return true;

                    case R.id.createListing:
                        Intent intent2 = new Intent(getApplicationContext(), CreateListing.class);
                        startActivity(intent2);
                        return true;

                    case R.id.myListings:
                        Intent intent3 = new Intent(getApplicationContext(), CustomerListingNav.class);
                        startActivity(intent3);
                        return true;


                    case R.id.home:
                        Intent intent4 = new Intent(getApplicationContext(), WelcomeCustomer.class);
                        startActivity(intent4);
                }

                return false;
            }
        });

        getJob();

    }

    private void getJob() {
            ref.child("Job").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Iterable<DataSnapshot> children = snapshot.getChildren();
                    for (DataSnapshot child : children) {
                        if (child.getKey().equals(jobId)) {
                           Job job = child.getValue(Job.class);
                            t4.setText(job.getJobTitle());
                            String price = String.valueOf(job.getQuote());
                            t2.setText(price);
                            t1.setText(job.getDescription());
                            t3.setText(job.getPriceBreakdown());

                            //Customer Image
                            storageReference = storage.getReferenceFromUrl("gs://fypdatabase-d9dfe.appspot.com"+job.getListing().getPhotos().get(0));
                            try {
                                final File file = File.createTempFile("image", "jpeg");
                                storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                        imageView.setImageBitmap(bitmap);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SelectedKeywordJob.this, "Image Failed to Load", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //Prof Images
                            storageReference = storage.getReferenceFromUrl("gs://fypdatabase-d9dfe.appspot.com"+job.getAfterPics().get(0));
                            try {
                                final File file = File.createTempFile("image", "jpeg");
                                storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                        img1.setImageBitmap(bitmap);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SelectedKeywordJob.this, "Image Failed to Load", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            storageReference = storage.getReferenceFromUrl("gs://fypdatabase-d9dfe.appspot.com"+job.getAfterPics().get(1));
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
                                        Toast.makeText(SelectedKeywordJob.this, "Image Failed to Load", Toast.LENGTH_SHORT).show();
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