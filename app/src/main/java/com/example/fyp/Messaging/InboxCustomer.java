package com.example.fyp.Messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.fyp.Adapters.InboxCustomerAdapter;
import com.example.fyp.Adapters.InboxProfessionalAdapter;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.ObjectClasses.Message;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InboxCustomer extends AppCompatActivity {

    //  ArrayList<Listing> listings = new ArrayList<Listing>();
    private FirebaseDatabase database;
    private DatabaseReference ref, ref2;
    // InboxProfessionalAdapter myAdapter;
    InboxCustomerAdapter myAdapter;
    private FirebaseUser user;
    private String uid;
    ArrayList<Message> allMessages = new ArrayList<Message>();
    ArrayList<Message> myMessages = new ArrayList<Message>();

    ArrayList<String> userList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_customer);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref2 = database.getReference();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myAdapter = new InboxCustomerAdapter(myMessages);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(myAdapter);

        getFromFirebase();
    }

    public void getFromFirebase() {
        ref.child("Message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Message message = child.getValue(Message.class);
                    allMessages.add(message);
                    getFromFirebase2();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

    public void getFromFirebase2() {
        ref2.child("Listing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Listing listing = child.getValue(Listing.class);

                    for (Message mes : allMessages) {
//                        if(myMessages.isEmpty()){
//                            myMessages.add(mes);
                        // }else{
                        if (mes.getListingId().equals(listing.getListingId()) && mes.getCustomerId().equals(uid)) {
                            if (myMessages.isEmpty()) {
                                myMessages.add(mes);

                            } else {
                               /// for (Message newMessage : myMessages) {
                                    if (mes.getListingId().equals(listing.getListingId())) {
                                      //  if (mes.getListingId().equals(newMessage.getListingId())) {

                                            System.out.println("Already Exists");
                                    } else {
                                        myMessages.add(mes);
                                    }
                                }
                                //    myMessages.add(mes);
                            }
                     //   }
                    }
                }

                myAdapter.notifyItemInserted(userList.size() - 1);
            }


        @Override
        public void onCancelled (@NonNull DatabaseError error){
            //   Log.m("DBE Error","Cancel Access DB");
        }
    });
}
}