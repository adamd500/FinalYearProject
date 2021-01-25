package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.fyp.Adapters.AcceptedConsultationAdapter;
import com.example.fyp.Adapters.MyAdapterCustomer;
import com.example.fyp.ObjectClasses.Consultation;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AcceptedConsultations extends AppCompatActivity {

    ArrayList<Consultation> consultations = new ArrayList<Consultation>();
    private FirebaseDatabase database;
    private DatabaseReference ref;
    AcceptedConsultationAdapter myAdapter;
    private FirebaseUser user;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_consultations);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myAdapter = new AcceptedConsultationAdapter(consultations);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(myAdapter);
        getFromFirebase();
    }

    public void getFromFirebase() {

        ref.child("Consultation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Consultation consultation = child.getValue(Consultation.class);

                    if (consultation.getProfessionalId().equals(uid) && consultation.isAccepted() == true) {
                        consultations.add(consultation);
                    }

                    myAdapter.notifyItemInserted(consultations.size() - 1);
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