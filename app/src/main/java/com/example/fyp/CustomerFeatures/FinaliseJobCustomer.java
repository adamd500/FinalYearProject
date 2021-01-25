package com.example.fyp.CustomerFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class FinaliseJobCustomer extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5,t6,t7;
    EditText e1;
    String jobId;
    private FirebaseDatabase database;
    private DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalise_job_customer);

        Intent intent = getIntent();
        jobId= intent.getStringExtra("id");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        t1=(TextView)findViewById(R.id.estimatedDuration);
        t2=(TextView)findViewById(R.id.actualDuration);
        t3=(TextView)findViewById(R.id.quotePrice);
        t4=(TextView)findViewById(R.id.actualPrice);
        t5=(TextView)findViewById(R.id.priceBreakdown);
        t6=(TextView)findViewById(R.id.startDate);
        t7=(TextView)findViewById(R.id.finisDate);

        e1=(EditText)findViewById(R.id.feedbackForProfessional);

        getJob();


    }

    public void getJob() {
        ref.child("Job").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(jobId)) {
                        Job job = child.getValue(Job .class);



                        t1.setText("Estimated Duration : "+job.getEstimatedDuration());
                        t2.setText("Actual Duration : "+job.getActualDuration());
                        t3.setText("Quoted Price : "+String.valueOf(job.getQuote()));
                        t4.setText("Actual Price : "+String.valueOf(job.getPrice()));
                        t5.setText("Price Breakdown : "+job.getPriceBreakdown());
                        t6.setText("Date Started : "+job.getStartDate());
                        t7.setText("Date Finished : "+job.getEndDate());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

    public void finaliseJob(View v){

        String feedbackForProfessional=e1.getText().toString();

        ref.child("Job").child(jobId).child("finished").setValue(true);
        ref.child("Job").child(jobId).child("feedbackFromCustomer").setValue(feedbackForProfessional);

        Intent intent = new Intent(this,PaymentPage.class);
        startActivity(intent);

    }
}