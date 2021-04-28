package com.example.fyp.CustomerFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.fyp.Adapters.JobKeywordAdapter;
import com.example.fyp.Messaging.InboxCustomer;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectedProfessionalPreviousJobs extends AppCompatActivity {

    String professionalId;
    ArrayList<Job> jobMatches =new ArrayList<Job>();
     FirebaseDatabase database;
     DatabaseReference ref;
    JobKeywordAdapter myAdapter;
    TextView t1,t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_professional_previous_jobs);

        Intent intent =getIntent();
        professionalId=intent.getStringExtra("id");

        database=FirebaseDatabase.getInstance();
        ref=database.getReference();

        t1=(TextView)findViewById(R.id.title);
        t2=(TextView)findViewById(R.id.priceRange);

        t1.setText("Previous Jobs");

        RecyclerView mRecyclerView=(RecyclerView)findViewById(R.id.my_adapter_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myAdapter= new JobKeywordAdapter(jobMatches);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(myAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(SelectedProfessionalPreviousJobs.this, InboxCustomer.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(SelectedProfessionalPreviousJobs.this, CustomerProfile.class);
                        startActivity(intent1);
                        return true;

                    case R.id.createListing:
                        Intent intent2 = new Intent(SelectedProfessionalPreviousJobs.this, CreateListing.class);
                        startActivity(intent2);
                        return true;

                    case R.id.myListings:
                        Intent intent3 = new Intent(SelectedProfessionalPreviousJobs.this, CustomerListingNav.class);
                        startActivity(intent3);
                        return true;


                    case R.id.home:
                        Intent intent4 = new Intent(SelectedProfessionalPreviousJobs.this, WelcomeCustomer.class);
                        startActivity(intent4);
                }

                return false;
            }
        });
        getJobs();

    }
    public void getJobs(){
        ref.child("Job").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot>children=snapshot.getChildren();
                for(DataSnapshot child:children){
                    Job job=child.getValue(Job.class);

                    if(job.isFinished() && job.getProfessionalId().equals(professionalId)) {
                        jobMatches.add(job);
                    }
                    myAdapter.notifyItemInserted(jobMatches.size()-1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });

    }
}