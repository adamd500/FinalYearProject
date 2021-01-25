package com.example.fyp.Messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Adapters.MessageAdapter;
import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.ObjectClasses.Message;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProfessionalSelectedConversation extends AppCompatActivity {

    String listingId, customerId, listingTitle,professionalId;
    TextView titleText;
    private FirebaseDatabase database;
    private DatabaseReference ref, ref2, reference;
    private FirebaseUser user;
    private String uid;
    EditText messageBox;
    MessageAdapter myAdapter;
    ArrayList<Message> messages;
    RecyclerView recyclerView;
    private String MESSAGE = "Message";
    Button button;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_selected_conversation);


        sdf = new SimpleDateFormat("dd.MM.yyyy  'at' HH:mm");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                readMessage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref2 = database.getReference(MESSAGE);
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                readMessage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        button = (Button) findViewById(R.id.sendButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendText();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();


        Intent intent = getIntent();
        listingId = intent.getStringExtra("listingId");
        customerId = intent.getStringExtra("customerId");
        listingTitle = intent.getStringExtra("listingTitle");

        titleText = (TextView) findViewById(R.id.textViewTitle);
        messageBox = (EditText) findViewById(R.id.message);

        getCustomer();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lin = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lin);
    }

    public void getCustomer() {
        ref.child("Customer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(customerId)) {
                        Customer customer = child.getValue(Customer.class);
                        titleText.setText("Listing Title : " + listingTitle + "\n Customer Name : " + customer.getName() + "\n");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void readMessage() {
        messages = new ArrayList<Message>();
        reference = database.getReference("Message");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if ((message.getProfessionalId().equals(uid)) && (message.getCustomerId().equals(customerId)) && (message.getListingId().equals(listingId))) {
                        messages.add(message);
                    }
                    myAdapter = new MessageAdapter(messages);
                    recyclerView.setAdapter(myAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void sendText() {
        String text = messageBox.getText().toString();
        if (!text.equals("")) {

            String currentDateAndTime = sdf.format(new Date());
            Message message = new Message(uid, customerId, text, listingId, currentDateAndTime);
            ref.child("Message").push().setValue(message);

            messageBox.setText("");

        } else {
            Toast.makeText(this, "You cannot send an empty message.", Toast.LENGTH_SHORT).show();

        }
    }
}