package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class FinaliseJobProfessional extends AppCompatActivity {

    TextView t1, t2;
    EditText e1, e2, e3, e4, e5;
    private String jobId;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private String uid;
    private Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalise_job_professional);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        Intent intent = getIntent();
        jobId = intent.getStringExtra("id");

        t1 = (TextView) findViewById(R.id.estimatedDuration);
        t2 = (TextView) findViewById(R.id.quotePrice);

        e1 = (EditText) findViewById(R.id.actualDuration);
        e2 = (EditText) findViewById(R.id.actualPrice);
        e3 = (EditText) findViewById(R.id.priceBreakdown);
        e4 = (EditText) findViewById(R.id.finishDate);
        e5 = (EditText) findViewById(R.id.feedbackForCustomer);

        getJob();
    }

    public void getJob(){
        ref.child("Job").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(jobId)) {
                        job = child.getValue(Job.class);
                        t1.setText("Estimated Duration : "+job.getEstimatedDuration());
                        String price = String.valueOf(job.getQuote());
                        t2.setText("Price Quoted : "+price);
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
        String actualDuration=e1.getText().toString();
        String actualPrice=e2.getText().toString();
        String priceBreakdown=e3.getText().toString();
        String finishDate=e4.getText().toString();
        String feedbackForCustomer=e5.getText().toString();

        CurrentJobs.myAdapter.notifyDataSetChanged();
        ref.child("Job").child(jobId).child("price").setValue(Integer.parseInt(actualPrice));
        ref.child("Job").child(jobId).child("actualDuration").setValue(actualDuration);
        ref.child("Job").child(jobId).child("priceBreakdown").setValue(priceBreakdown);
        ref.child("Job").child(jobId).child("endDate").setValue(finishDate);
        ref.child("Job").child(jobId).child("feedbackFromProfessional").setValue(feedbackForCustomer);

        Toast.makeText(this, "Job has been submitted. Once customer has finalised the funds will be released.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this,WelcomeProfessional.class);
        startActivity(intent);

    }
}