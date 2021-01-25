package com.example.fyp.CustomerFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.fyp.Adapters.CurrentJobsAdapter;
import com.example.fyp.Adapters.JobsForFinalisingAdapter;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JobsAwaitingFinalisation extends AppCompatActivity {

    ArrayList<Job> jobs = new ArrayList<Job>();
    private FirebaseDatabase database;
    private DatabaseReference ref;
    JobsForFinalisingAdapter myAdapter;
    private FirebaseUser user;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_awaiting_finalisation);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myAdapter = new JobsForFinalisingAdapter(jobs);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(myAdapter);
        getFromFirebase();
    }

    public void getFromFirebase() {

        ref.child("Job").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Job job = child.getValue(Job.class);

                    if ((job.getCustomerId().equals(uid)) && (job.getPrice() != 0) && (!job.isFinished())) {
                        jobs.add(job);
                    }

                    myAdapter.notifyItemInserted(jobs.size() - 1);
                    //myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }
}