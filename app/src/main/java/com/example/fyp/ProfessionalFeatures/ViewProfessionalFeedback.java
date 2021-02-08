package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fyp.Adapters.InboxCustomerAdapter;
import com.example.fyp.Adapters.ProfessionalFeedbackAdapter;
import com.example.fyp.ObjectClasses.Professional;
import com.example.fyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ViewProfessionalFeedback extends AppCompatActivity {

    private String professionalId;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private TextView t1, t2, t3, t4, t5;
    private ProfessionalFeedbackAdapter myAdapter;
    private ArrayList<String> feedback = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_professional_profile);

        Intent intent = getIntent();
        professionalId = intent.getStringExtra("professionalId");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        t1 = (TextView) findViewById(R.id.textViewName);
        t2 = (TextView) findViewById(R.id.textViewCommunicationRating);
        t3 = (TextView) findViewById(R.id.textViewPunctualityRating);
        t4 = (TextView) findViewById(R.id.textViewWorkQualityRating);
        t5 = (TextView) findViewById(R.id.textViewOverallCustomerSatisfactionRating);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myAdapter = new ProfessionalFeedbackAdapter(feedback);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(myAdapter);

        getProfessional();
    }

    public void getProfessional() {
        ref.child("Professional").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(professionalId)) {
                        Professional professional = child.getValue(Professional.class);
                        int jobsCompleted = professional.getJobsCompleted();

                        int communication = professional.getCommunication() / jobsCompleted;
                        int puntuality = professional.getPunctuality() / jobsCompleted;
                        int workQuality = professional.getWorkQuality() / jobsCompleted;
                        int overAll = professional.getOverallCustomerSatisfaction() / jobsCompleted;

                        t1.setText(professional.getName());
                        t2.setText("Communication Rating : " + String.valueOf(communication) + "%");
                        t3.setText("Punctuality Rating : " + String.valueOf(puntuality) + "%");
                        t4.setText("Work Quality Rating : " + String.valueOf(workQuality) + "%");
                        t5.setText("Overall Customer Satisfaction Rating : " + String.valueOf(overAll) + "%");

                        for (String fb : professional.getFeedback()) {
                            feedback.add(fb);
                        }
                    }
                    myAdapter.notifyItemInserted(feedback.size() - 1);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

}