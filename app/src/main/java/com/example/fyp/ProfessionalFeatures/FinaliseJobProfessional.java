package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.CustomerFeatures.CreateListing;
import com.example.fyp.Messaging.InboxProfessional;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class FinaliseJobProfessional extends AppCompatActivity {

    TextView t1, t2, t3;
    EditText e1, e2, e3, e4, e5;
    private String jobId;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private String uid;
    private Job job;
    Spinner spinner;
     FirebaseStorage storage;
     StorageReference storageReference;
    String finishDate;
    DatePickerDialog.OnDateSetListener dateSetListener;
    private static final int PICK_IMAGE_REQUEST = 22;
    Uri filePath;
    Uri filePath2;
    String picPath1, picPath2;
    ImageView img1, img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalise_job_professional);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        Intent intent = getIntent();
        jobId = intent.getStringExtra("id");

        storage= FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        img1 = (ImageView) findViewById(R.id.imageView);
        img2 = (ImageView) findViewById(R.id.imageView2);

        t1 = (TextView) findViewById(R.id.estimatedDuration);
        t2 = (TextView) findViewById(R.id.quotePrice);
        t3 = (TextView) findViewById(R.id.finishDate);
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(FinaliseJobProfessional.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, dateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
                dialog.show();
                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth1) {
                        month1 = month1 + 1;

                        String dateStr = dayOfMonth1 + "/" + month1 + "/" + year1;
                        t3.setText(dateStr);

                    }
                };
            }
        });

        e1 = (EditText) findViewById(R.id.actualDuration);
        e2 = (EditText) findViewById(R.id.actualPrice);
        e3 = (EditText) findViewById(R.id.priceBreakdown);
        e5 = (EditText) findViewById(R.id.feedbackForCustomer);

        spinner = (Spinner) findViewById(R.id.spinner);

        getJob();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inbox:
                        Intent intent = new Intent(FinaliseJobProfessional.this, InboxProfessional.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(FinaliseJobProfessional.this, ProfileHomePage.class);
                        startActivity(intent1);
                        return true;

                    case R.id.work:
                        Intent intent2 = new Intent(FinaliseJobProfessional.this, WorkHomepage.class);
                        startActivity(intent2);
                        return true;

                    case R.id.stats:
                        Intent intent3 = new Intent(FinaliseJobProfessional.this, ViewProfessionalFeedback.class);
                        intent3.putExtra("professionalId", uid);
                        startActivity(intent3);
                        return true;

                    case R.id.home:
                        Intent intent4 = new Intent(FinaliseJobProfessional.this, WelcomeProfessional.class);
                        startActivity(intent4);
                }

                return false;
            }
        });
    }

    public void getJob() {
        ref.child("Job").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(jobId)) {
                        job = child.getValue(Job.class);
                        t1.setText(job.getEstimatedDuration());
                        String price = String.valueOf(job.getQuote());
                        t2.setText(price);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });

    }

    public void finishJob(View v) {

        ref.child("Job").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(jobId)) {
                        job = child.getValue(Job.class);

                        String actualDuration = e1.getText().toString();
                        String actualPrice = e2.getText().toString();
                        String priceBreakdown = e3.getText().toString();
                        String finishDate = t3.getText().toString();
                        String feedbackForCustomer = e5.getText().toString();
                        ArrayList<String>pics = new ArrayList<String>();

                        CurrentJobs.myAdapter.notifyDataSetChanged();

                        if(!picPath1.isEmpty()){
                            pics.add(picPath1);
                        }
                        if(!picPath2.isEmpty()){
                            pics.add(picPath2);
                        }

                        job.setPrice(Integer.parseInt(actualPrice));
                        job.setActualDuration(actualDuration + spinner.getSelectedItem());
                        job.setPriceBreakdown(priceBreakdown);
                        job.setEndDate(finishDate);
                        job.setFeedbackFromProfessional(feedbackForCustomer);
                        job.setAfterPics(pics);

                        ref.child("Job").child(jobId).setValue(job);

                        Intent intent = new Intent(FinaliseJobProfessional.this, JobFinishedPage.class);
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

    public void selectImage(View view) {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);

    }
    public void selectImage2(View view) {

        // Defining Implicit Intent to mobile gallery
        Intent intent2 = new Intent();
        intent2.setType("image/*");
        intent2.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent2, "Select Image from here..."), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null) {

            if (filePath == null) {
                filePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    img1.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

                filePath2 = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    img2.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void uploadImage(View v) {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("completedJobImages/"+jobId+" image1");
            picPath1 = ref.getPath();
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    // dbRef.child("User").child(uid).child("profileImageUri").setValue(filePath);

                                    Toast.makeText(FinaliseJobProfessional.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(FinaliseJobProfessional.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
        }

        if (filePath2 != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("completedJobImages/"+jobId+" image2");
            picPath2 = ref.getPath();
            ref.putFile(filePath2)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    // dbRef.child("User").child(uid).child("profileImageUri").setValue(filePath);

                                    Toast.makeText(FinaliseJobProfessional.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(FinaliseJobProfessional.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
        }
    }

}