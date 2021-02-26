package com.example.fyp.ProfessionalFeatures;

import android.os.Bundle;

import com.example.fyp.Adapters.MyAdapterProfessional;
import com.example.fyp.ObjectClasses.Conversation;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.ObjectClasses.Professional;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BrowseJobs extends AppCompatActivity {

    ArrayList<Listing> listings = new ArrayList<Listing>();
    ArrayList<Conversation> conversations = new ArrayList<Conversation>();

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private DatabaseReference ref2;

    MyAdapterProfessional myAdapter;
    private FirebaseUser user;
    Professional professional;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brows_jobs);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recylcerViewProf);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref2 = database.getReference();

        getProfessional();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myAdapter = new MyAdapterProfessional(listings);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(myAdapter);
        getProfessionalsConversations();

    }

    public void getProfessional() {
        ref.child("Professional").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(uid)) {
                        professional = child.getValue(Professional.class);
                        //  TextView tv=(TextView)findViewById(R.id.textView9);
                        // tv.setText(professional.getName());
                    }
                    //myAdapter.notifyItemInserted(listings.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

    public void getProfessionalsConversations() {
        ref.child("Conversation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Conversation conversation = child.getValue(Conversation.class);
                    if (conversation.getProfessionalId().equals(uid)) {
                        //getListings(conversation);
                        conversations.add(conversation);
                    }
                }
                getListings();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }


    public void getListings() {

        ref.child("Listing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Listing listing = child.getValue(Listing.class);

                    for(Conversation con : conversations) {

                        if ((listing.getTradeSector().equalsIgnoreCase(professional.getTrade())) && (listing.isActive()) && (!listing.getListingId().equals(con.getListingId()))) {

                            listings.add(listing);

                        }

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