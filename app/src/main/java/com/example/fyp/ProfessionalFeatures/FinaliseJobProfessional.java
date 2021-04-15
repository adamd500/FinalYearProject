package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.Calendar;

public class FinaliseJobProfessional extends AppCompatActivity {

    TextView t1, t2,t3;
    EditText e1, e2, e3, e4, e5;
    private String jobId;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private String uid;
    private Job job;
    Spinner spinner;
    String finishDate;
    DatePickerDialog.OnDateSetListener dateSetListener;

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

        spinner=(Spinner)findViewById(R.id.spinner);

        getJob();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
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
                        intent3.putExtra("professionalId",uid);
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

    public void getJob(){
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
        String actualDuration=e1.getText().toString();
        String actualPrice=e2.getText().toString();
        String priceBreakdown=e3.getText().toString();
        String finishDate=t3.getText().toString();
        String feedbackForCustomer=e5.getText().toString();

        CurrentJobs.myAdapter.notifyDataSetChanged();
        ref.child("Job").child(jobId).child("price").setValue(Integer.parseInt(actualPrice));
        ref.child("Job").child(jobId).child("actualDuration").setValue(actualDuration+spinner.getSelectedItem());
        ref.child("Job").child(jobId).child("priceBreakdown").setValue(priceBreakdown);
        ref.child("Job").child(jobId).child("endDate").setValue(finishDate);
        ref.child("Job").child(jobId).child("feedbackFromProfessional").setValue(feedbackForCustomer);

        Toast.makeText(this, "Job has been submitted. Once customer has finalised the funds will be released.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this,WelcomeProfessional.class);
        startActivity(intent);

    }
}