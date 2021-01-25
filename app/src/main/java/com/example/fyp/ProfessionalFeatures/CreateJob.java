package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fyp.ObjectClasses.Consultation;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateJob extends AppCompatActivity {

    private String consultationId;
     Consultation consultation;
     Listing listing;
    private FirebaseDatabase database;
    private DatabaseReference ref,ref2;
    private EditText e1,e2,e3,e4,e5,e6;
    private FirebaseUser user;
    private String uid;
    private static final String Job = "Job";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref2 = database.getReference(Job);

        Intent intent = getIntent();
        consultationId=intent.getStringExtra("id");

        e1= (EditText)findViewById(R.id.location);
        e2= (EditText)findViewById(R.id.duration);
        e3= (EditText)findViewById(R.id.description);
        e4= (EditText)findViewById(R.id.quote);
        e5= (EditText)findViewById(R.id.startDate);
        e6= (EditText)findViewById(R.id.jobTitle);


        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        getConsultation();
        getListing();

    }

    public void getConsultation(){
        ref.child("Consultation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(consultationId)) {
                        consultation = child.getValue(Consultation.class);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }
    public void getListing(){
        ref.child("Listing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(consultation.getListingId())) {
                        listing = child.getValue(Listing.class);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }
    public void createJob(View v){
        String jobId = ref2.push().getKey();

        String location=e1.getText().toString();
        String duration=e2.getText().toString();
        String description=e3.getText().toString();
        int quote=Integer.parseInt(e4.getText().toString());
        String startDate=e5.getText().toString();
        String title =e6.getText().toString();

        Job job=new Job(jobId,  consultation.getCustomerId(),  uid,  consultation,  listing,  0,  location,  duration,
                description,  quote,  "t",  "t",false,startDate,"s","s","s",title);

        ref2.child(jobId).setValue(job);

        Toast.makeText(this, "Job has been created", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this,WelcomeProfessional.class);
        startActivity(intent);
    }
}