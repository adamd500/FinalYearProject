package com.example.fyp.CustomerFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.fyp.Adapters.AdapterConsultationCustomer;
import com.example.fyp.ObjectClasses.Consultation;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerAllConsultations extends AppCompatActivity {
    ArrayList<Consultation> consultations=new ArrayList<Consultation>();
    private FirebaseDatabase database;
    private DatabaseReference ref;
    public static AdapterConsultationCustomer adapter;
    private FirebaseUser user;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_all_consultations);

        user= FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();

        RecyclerView mRecyclerView=(RecyclerView)findViewById(R.id.recyclerViewConsulations);

        database=FirebaseDatabase.getInstance();
        ref=database.getReference();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter= new AdapterConsultationCustomer(consultations);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter);
        getFromFirebase();
    }

    public void getFromFirebase(){
        ref.child("Consultation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot>children=snapshot.getChildren();
                for(DataSnapshot child:children){
                    Consultation consultation=child.getValue(Consultation.class);
                    if( (consultation.getCustomerId().equals(uid)) && (!consultation.isFinished()) && (!consultation.isAccepted())) {
                        consultations.add(consultation);
                    }
                    adapter.notifyItemInserted(consultations.size()-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }
}