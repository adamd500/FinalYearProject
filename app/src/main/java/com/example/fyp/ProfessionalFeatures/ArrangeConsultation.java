package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fyp.CustomerFeatures.CreateListing;
import com.example.fyp.CustomerFeatures.WelcomeCustomer;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ArrangeConsultation extends AppCompatActivity {

    private String listingId, dateStr, timeStr, messageStr;
    private EditText date, time, message;
    private FirebaseUser user;
    private String uid;
    private String customerId;
    Listing listing;
    private static final String Consultation = "Consultation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange_consultation);

        Intent i = getIntent();
        listingId = i.getStringExtra("listingId");

        date = (EditText) findViewById(R.id.dateEditText);
        time = (EditText) findViewById(R.id.timeEditText);
        message = (EditText) findViewById(R.id.editTextMessage);


        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        getListing();

    }

    public void getListing() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference  ref = database.getReference();

        ref.child("Listing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(listingId)) {
                        listing = child.getValue(Listing.class);
                        customerId=listing.getCustomerUsername();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

    public void sendConsultationDetails(View v) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref2 = database.getReference(Consultation);

        String consultationId = ref2.push().getKey();
        dateStr = date.getText().toString();
        timeStr = time.getText().toString();
        messageStr = message.getText().toString();

        Consultation consultation = new Consultation(dateStr, timeStr, messageStr, false, false, consultationId);
        consultation.setCustomerId(customerId);
        consultation.setProfessionalId(uid);
        consultation.setListingId(listingId);

        ref2.child(consultationId).setValue(consultation);

        Toast.makeText(ArrangeConsultation.this, "Consultation Successfully Logged, Client will be alerted ", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ArrangeConsultation.this, WelcomeProfessional.class);
        startActivity(intent);
    }
}