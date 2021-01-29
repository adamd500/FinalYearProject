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
import com.example.fyp.ObjectClasses.Conversation;
import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.ObjectClasses.Message;
import com.example.fyp.ObjectClasses.Professional;
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

public class StartConversation2 extends AppCompatActivity {

    String listingId, customerId, title;
    private FirebaseDatabase database;
    private DatabaseReference ref, ref2, reference;
    private FirebaseUser user;
    private String uid;
    EditText messageBox;
    TextView titleText;
    MessageAdapter myAdapter;
    ArrayList<Message> messages;
    RecyclerView recyclerView;
    private String Conversation = "Conversation";
    Button button;
    private SimpleDateFormat sdf;
    ArrayList<Message> conversationMessages = new ArrayList<Message>();
    String conversationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_conversation2);
        sdf = new SimpleDateFormat("dd.MM.yyyy  'at' HH:mm");

        Intent intent = getIntent();
        listingId = intent.getStringExtra("listingId");
        customerId = intent.getStringExtra("customerId");
        title = intent.getStringExtra("listingTitle");

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
        ref2 = database.getReference(Conversation);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        titleText = (TextView) findViewById(R.id.textViewTitle);
        messageBox = (EditText) findViewById(R.id.message);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lin = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lin);
        getCustomer();
    }

    public void getCustomer() {
        ref.child("Customer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(customerId)) {
                        Customer customer = child.getValue(Customer.class);

                        getProfessional(customer.getName());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

    public void getProfessional(String customerName) {
        ref.child("Professional").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(uid)) {
                        Professional professional = child.getValue(Professional.class);

                        String currentDateAndTime = sdf.format(new Date());
                        String content = "Re : " + title;

                        conversationId = ref2.push().getKey();
                        Message message = new Message(conversationId, content,currentDateAndTime, professional.getName());
                        conversationMessages.add(message);

                        Conversation conversation = new Conversation(conversationId,customerId, customerName, listingId, title, uid, professional.getName(), conversationMessages);
                        ref2.child(conversationId).setValue(conversation);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

    private void readMessage() {
        messages = new ArrayList<Message>();
        reference = database.getReference("Conversation");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Conversation conversation = dataSnapshot.getValue(Conversation.class);
                    if ((conversation.getProfessionalId().equals(uid)) && (conversation.getCustomerId().equals(customerId)) && (conversation.getListingId().equals(listingId))) {
                       // messages=conversation.getMessages();
                        for(Message mes : conversation.getMessages()){
                            messages.add(mes);
                        }
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

    public void sendText(View v) {
        String text = messageBox.getText().toString();
        if (!text.equals("")) {
            ref.child("Professional").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Professional prof = snapshot.getValue(Professional.class);

                            String currentDateAndTime = sdf.format(new Date());

                            Message message = new Message(conversationId, text,  currentDateAndTime, prof.getName());
                            addToConversation(message);
                           // ref.child("Message").push().setValue(message);

                            messageBox.setText("");
                        }



                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //   Log.m("DBE Error","Cancel Access DB");
                }
            });


        } else {
            Toast.makeText(this, "You cannot send an empty message.", Toast.LENGTH_SHORT).show();

        }
    }

    public void addToConversation(Message message){
        ref.child("Conversation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Conversation conversation = child.getValue(Conversation.class);
                        if(conversation.getConversationId().equals(message.getConversationId())) {
                            ArrayList<Message>updatedConversation= conversation.getMessages();
                            updatedConversation.add(message);
                            ref.child("Conversation").child(conversationId).child("messages").setValue(updatedConversation);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }



}