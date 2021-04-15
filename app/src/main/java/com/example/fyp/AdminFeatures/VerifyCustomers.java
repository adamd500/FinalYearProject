package com.example.fyp.AdminFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.fyp.Adapters.AdapterConsultationCustomer;
import com.example.fyp.Adapters.CustomerVerificationAdapter;
import com.example.fyp.Adapters.MyAdapterCustomer;
import com.example.fyp.ObjectClasses.Consultation;
import com.example.fyp.ObjectClasses.Customer;
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

public class VerifyCustomers extends AppCompatActivity {

    ArrayList<Customer> customers=new ArrayList<Customer>();
    private FirebaseDatabase database;
    private DatabaseReference ref;
    public static CustomerVerificationAdapter myAdapter;
    private FirebaseUser user;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_customers);

        user= FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();

        RecyclerView mRecyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        database=FirebaseDatabase.getInstance();
        ref=database.getReference();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myAdapter= new CustomerVerificationAdapter(customers);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(myAdapter);
        getFromFirebase();
    }
    public void getFromFirebase(){
        ref.child("Customer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot>children=snapshot.getChildren();
                for(DataSnapshot child:children){
                    Customer customer=child.getValue(Customer.class);
                    if(!customer.isIdVerified()) {
                        customers.add(customer);
                    }
                    myAdapter.notifyItemInserted(customers.size()-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }
}