package com.example.fyp.AdminFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.fyp.Adapters.CustomerVerificationAdapter;
import com.example.fyp.Adapters.ProfessionalVerificationAdapter;
import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.ObjectClasses.Professional;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VerifyProfessionals extends AppCompatActivity {

    ArrayList<Professional> professionals = new ArrayList<Professional>();
    private FirebaseDatabase database;
    private DatabaseReference ref;
    ProfessionalVerificationAdapter myAdapter;
    private FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_professionals);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myAdapter = new ProfessionalVerificationAdapter(professionals);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(myAdapter);
        getFromFirebase();
    }

    public void getFromFirebase() {
        ref.child("Professional").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Professional professional = child.getValue(Professional.class);
//                    if (!professional.getGardaVetImage().equals("s") || !professional.getIdImage().equals("s")
//                            || !professional.getSafePassImage().equals("s") || !professional.getSelfieImage().equals("s")) {

                        if (!professional.isIdVerified() || !professional.isSafePassVer() || !professional.isGardaVetVer()) {
                            professionals.add(professional);
                        }
//                    }
//
                    myAdapter.notifyItemInserted(professionals.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }
}