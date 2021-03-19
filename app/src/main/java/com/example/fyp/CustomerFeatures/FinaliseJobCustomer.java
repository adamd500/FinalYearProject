package com.example.fyp.CustomerFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Messaging.InboxCustomer;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ObjectClasses.Professional;
import com.example.fyp.ProfessionalFeatures.FinaliseJobProfessional;
import com.example.fyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinaliseJobCustomer extends AppCompatActivity {

    int price;
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
    Thread thread;
    String clientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalise_job_customer);

        Stripe.apiKey = "sk_test_51IFKQvKgTR7yUeIexy2I5Th0pv3OGDiM088vBZY2YFHLkJO1uxZrCesiQoGzUewSLdkwnkcETWhlwk5bGlUCsrLB00FF8KPznk";


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

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList paymentMethodTypes = new ArrayList();
                paymentMethodTypes.add("card");
//                int price1 = Integer.valueOf(price);
//                Double p = price1 * 0.15;
//                int fee = (int) Math.round(p);
                Map<String, Object> params = new HashMap<>();
                params.put("payment_method_types", paymentMethodTypes);
                params.put("amount", price);
                params.put("currency", "eur");
                params.put("application_fee_amount", 500);
                Map<String, Object> transferDataParams = new HashMap<>();
                transferDataParams.put("destination", "acct_1IMGGO4CaypLLH6w");
                params.put("transfer_data", transferDataParams);
                try {
                    PaymentIntent paymentIntent = PaymentIntent.create(params);
                    clientSecret = paymentIntent.getClientSecret();

                } catch (StripeException e) {
                    e.printStackTrace();
                }

            }
        });

        getJob();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(FinaliseJobCustomer.this, InboxCustomer.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(FinaliseJobCustomer.this, CustomerProfile.class);
                        startActivity(intent1);
                        return true;

                    case R.id.createListing:
                        Intent intent2 = new Intent(FinaliseJobCustomer.this, CreateListing.class);
                        startActivity(intent2);
                        return true;

                    case R.id.myListings:
                        Intent intent3 = new Intent(FinaliseJobCustomer.this, CustomerListingNav.class);
                        startActivity(intent3);
                        return true;


                    case R.id.home:
                        Intent intent4 = new Intent(FinaliseJobCustomer.this, WelcomeCustomer.class);
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
                        Job job = child.getValue(Job.class);
                        price=job.getPrice();

 //                       thread.start();
                        t1.setText("Estimated Duration : " + job.getEstimatedDuration());
                        t2.setText("Actual Duration : " + job.getActualDuration());
                        t3.setText("Quoted Price : " + String.valueOf(job.getQuote()));
                        t4.setText("Actual Price : " + price);
                        t5.setText("Price Breakdown : " + job.getPriceBreakdown());
                        t6.setText("Date Started : " + job.getStartDate());
                        t7.setText("Date Finished : " + job.getEndDate());
                        thread.start();
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
        //thread.start();

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

                    Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                    intent.putExtra("clientSecret",clientSecret);
                    intent.putExtra("jobId",jobId);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //   Log.m("DBE Error","Cancel Access DB");
                }
            });

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
                ref.child("Job").child(jobId).child("feedbackFromCustomer").setValue(feedbackForProfessional);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }


}