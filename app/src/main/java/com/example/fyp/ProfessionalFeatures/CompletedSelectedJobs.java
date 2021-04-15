package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.fyp.Adapters.CompletedJobsAdapter;
import com.example.fyp.Adapters.CurrentJobsAdapter;
import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CompletedSelectedJobs extends AppCompatActivity {
    ArrayList<Job> jobs = new ArrayList<Job>();
    private FirebaseDatabase database;
    private DatabaseReference ref;
    static CompletedJobsAdapter myAdapter;
    private FirebaseUser user;
    private String uid;
    TextView t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_selected_jobs);


        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Intent intent = getIntent();
        String month=intent.getStringExtra("month");

        if(month.equalsIgnoreCase("january")){
            jobs = Graph2.january;
        }
        if(month.equalsIgnoreCase("february")){
            jobs = Graph2.february;
        }
        if(month.equalsIgnoreCase("march")){
            jobs = Graph2.march;
        }
        if(month.equalsIgnoreCase("april")){
            jobs = Graph2.april;
        }
        if(month.equalsIgnoreCase("may")){
            jobs = Graph2.january;
        }
        if(month.equalsIgnoreCase("june")){
            jobs = Graph2.may;
        }
        if(month.equalsIgnoreCase("july")){
            jobs = Graph2.july;
        }
        if(month.equalsIgnoreCase("august")){
            jobs = Graph2.august;
        }
        if(month.equalsIgnoreCase("september")){
            jobs = Graph2.september;
        }
        if(month.equalsIgnoreCase("october")){
            jobs = Graph2.october;
        }
        if(month.equalsIgnoreCase("november")){
            jobs = Graph2.november;
        }
        if(month.equalsIgnoreCase("december")){
            jobs = Graph2.december;
        }
        if(month.equalsIgnoreCase("whole year")){
            getFromFirebase();
        }


       myAdapter = new CompletedJobsAdapter(jobs);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(myAdapter);
//        getFromFirebase();
        t1=(TextView)findViewById(R.id.textView5);
        t1.setText("Jobs from "+month);

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

    private void getFromFirebase() {

        ref.child("Job").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Job job = child.getValue(Job.class);

                    if (job.getProfessionalId().equals(uid) && job.isFinished()) {
                        jobs.add(job);
                    }
                    myAdapter.notifyItemInserted(jobs.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }
}