package com.example.fyp.CustomerFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.fyp.Adapters.MyAdapterCustomer;
import com.example.fyp.Messaging.InboxCustomer;
import com.example.fyp.ObjectClasses.Listing;
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

public class PreviousListings extends AppCompatActivity {
    ArrayList<Listing> listings = new ArrayList<Listing>();
    private FirebaseDatabase database;
    private DatabaseReference ref;
    MyAdapterCustomer myAdapter;
    private FirebaseUser user;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_listings);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_adapter_view);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myAdapter = new MyAdapterCustomer(listings);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(myAdapter);
        getFromFirebase();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inbox:
                        Intent intent = new Intent(PreviousListings.this, InboxCustomer.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(PreviousListings.this, CustomerProfile.class);
                        startActivity(intent1);
                        return true;

                    case R.id.createListing:
                        Intent intent2 = new Intent(PreviousListings.this, CreateListing.class);
                        startActivity(intent2);
                        return true;

                    case R.id.myListings:
                        Intent intent3 = new Intent(PreviousListings.this, CustomerListingNav.class);
                        startActivity(intent3);
                        return true;


                    case R.id.home:
                        Intent intent4 = new Intent(PreviousListings.this, WelcomeCustomer.class);
                        startActivity(intent4);
                }

                return false;
            }
        });
    }

    public void getFromFirebase() {
        ref.child("Listing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Listing listing = child.getValue(Listing.class);
                    if (listing.getCustomerUsername().equals(uid) && !listing.isActive()) {
                        listings.add(listing);
                    }
                    myAdapter.notifyItemInserted(listings.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }
}