package com.example.fyp.Messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Adapters.MessageAdapter;
import com.example.fyp.ObjectClasses.Conversation;
import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.ObjectClasses.Message;
import com.example.fyp.ProfessionalFeatures.ViewProfessionalFeedback;
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

public class CustomerSelectedConversation extends AppCompatActivity {

    String listingId, professionalId,conversationId;
    TextView titleText, profText;
    private FirebaseDatabase database;
    private DatabaseReference ref, ref2, reference;
    private FirebaseUser user;
    private String uid;
    EditText messageBox;
    MessageAdapter myAdapter;
    ArrayList<Message> messages;
    RecyclerView recyclerView;
    private String MESSAGE = "Message";

    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_selected_conversation);

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

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();


        Intent intent = getIntent();
        listingId = intent.getStringExtra("listingId");
        professionalId = intent.getStringExtra("professionalId");
        conversationId = intent.getStringExtra("conversationId");

        titleText = (TextView) findViewById(R.id.textViewTitle);
        profText = (TextView) findViewById(R.id.customer);

        messageBox = (EditText) findViewById(R.id.message);

        getConversation();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lin = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lin);
    }

    public void getConversation() {
        ref.child("Conversation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(conversationId)) {
                        Conversation conversation = child.getValue(Conversation.class);
                        titleText.setText(conversation.getListingTitle());
                        profText.setText(conversation.getProfessionalName());
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
        reference = database.getReference("Conversation");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Conversation conversation = dataSnapshot.getValue(Conversation.class);
                    if ((conversation.getCustomerId().equals(uid)) && (conversation.getProfessionalId().equals(professionalId)) && (conversation.getListingId().equals(listingId))) {
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
            ref.child("Customer").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Customer customer = snapshot.getValue(Customer.class);

                    String currentDateAndTime = sdf.format(new Date());

                    Message message = new Message(conversationId, text, currentDateAndTime, customer.getName());
                    addToConversation(message);

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

    public void viewProfessionalProfile(View view) {
        Intent intent = new Intent(this, ViewProfessionalFeedback.class);
        intent.putExtra("professionalId",professionalId);
        startActivity(intent);
    }
}