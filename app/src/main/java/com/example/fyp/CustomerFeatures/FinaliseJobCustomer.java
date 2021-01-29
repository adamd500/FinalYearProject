package com.example.fyp.CustomerFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Adapters.JobsForFinalisingAdapter;
import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ObjectClasses.Professional;
import com.example.fyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

public class FinaliseJobCustomer extends AppCompatActivity {

    TextView t1, t2, t3, t4, t5, t6, t7;
    EditText e1, e2, e3, e4, e5;
    String jobId;
    private FirebaseDatabase database;
    private DatabaseReference ref, ref2;
    int punctuality;
    int communication;
    int workQuality;
    int overallCustomerSatisfaction;
    String feedbackForProfessional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalise_job_customer);

        Intent intent = getIntent();
        jobId = intent.getStringExtra("id");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref2 = database.getReference();

        t1 = (TextView) findViewById(R.id.estimatedDuration);
        t2 = (TextView) findViewById(R.id.actualDuration);
        t3 = (TextView) findViewById(R.id.quotePrice);
        t4 = (TextView) findViewById(R.id.actualPrice);
        t5 = (TextView) findViewById(R.id.priceBreakdown);
        t6 = (TextView) findViewById(R.id.startDate);
        t7 = (TextView) findViewById(R.id.finisDate);

        e1 = (EditText) findViewById(R.id.feedbackForProfessional);
        e2 = (EditText) findViewById(R.id.punctuality);
        e3 = (EditText) findViewById(R.id.communication);
        e4 = (EditText) findViewById(R.id.workQuality);
        e5 = (EditText) findViewById(R.id.overallSatisfaction);

        getJob();


    }

    public void getJob() {
        ref.child("Job").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(jobId)) {
                        Job job = child.getValue(Job.class);

                        t1.setText("Estimated Duration : " + job.getEstimatedDuration());
                        t2.setText("Actual Duration : " + job.getActualDuration());
                        t3.setText("Quoted Price : " + String.valueOf(job.getQuote()));
                        t4.setText("Actual Price : " + String.valueOf(job.getPrice()));
                        t5.setText("Price Breakdown : " + job.getPriceBreakdown());
                        t6.setText("Date Started : " + job.getStartDate());
                        t7.setText("Date Finished : " + job.getEndDate());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

    public void finaliseJob(View v) {
        punctuality = Integer.parseInt(e2.getText().toString());
        communication = Integer.parseInt(e3.getText().toString());
        workQuality = Integer.parseInt(e4.getText().toString());
        overallCustomerSatisfaction = Integer.parseInt(e5.getText().toString());
        feedbackForProfessional = e1.getText().toString();

        if (punctuality > 100 || communication > 100 || workQuality > 100 || overallCustomerSatisfaction > 100 || punctuality < 0 ||
                communication < 0 || workQuality < 0 || overallCustomerSatisfaction < 0 || feedbackForProfessional.isEmpty()) {

            Toast.makeText(FinaliseJobCustomer.this, "Incorrect Number Provided, Ensure all fields are filled and numbers range between 0 and 100.", Toast.LENGTH_SHORT).show();

        } else {
            ref.child("Job").child(jobId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Job job = snapshot.getValue(Job.class);
                    getProfessional(job.getProfessionalId(), job.getPrice());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //   Log.m("DBE Error","Cancel Access DB");
                }
            });
            Intent intent = new Intent(this, PaymentPage.class);
            startActivity(intent);
        }
    }

    public void getProfessional(String professionalId, int price) {
        ref2.child("Professional").child(professionalId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Professional professional = snapshot.getValue(Professional.class);

                int newJobsCompleted = professional.getJobsCompleted() + 1;
                int newPunctuality = professional.getPunctuality() + punctuality;
                int newCommunication = professional.getCommunication() + communication;
                int newWorkQuality = professional.getWorkQuality() + workQuality;
                int newOverallCustomerSatisfaction = professional.getOverallCustomerSatisfaction() + overallCustomerSatisfaction;
                int newTotalEarned = professional.getTotalEarned() + price;

                List<String> feedbackList = professional.getFeedback();
                feedbackList.add(feedbackForProfessional);

                JobsAwaitingFinalisation.myAdapter.notifyDataSetChanged();

                professional.setJobsCompleted(newJobsCompleted);
                professional.setPunctuality(newPunctuality);
                professional.setCommunication(newCommunication);
                professional.setWorkQuality(newWorkQuality);
                professional.setOverallCustomerSatisfaction(newOverallCustomerSatisfaction);
                professional.setTotalEarned(newTotalEarned);
                professional.setFeedback(feedbackList);

                ref2.child("Professional").child(professionalId).setValue(professional);
                ref.child("Job").child(jobId).child("finished").setValue(true);
                ref.child("Job").child(jobId).child("feedbackFromCustomer").setValue(feedbackForProfessional);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }


}