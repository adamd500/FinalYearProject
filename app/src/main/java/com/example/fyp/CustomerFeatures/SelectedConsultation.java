package com.example.fyp.CustomerFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.fyp.ObjectClasses.Consultation;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class SelectedConsultation extends AppCompatActivity {

    private TextView title,txtViewListingDetails,txtViewConsultationDetails,txtViewTime,txtViewDate,textViewMessage;
    private String consultationId;
    private Consultation consultation;
    private Listing listing;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_consultation);

        Intent intent =getIntent();
        consultationId=intent.getStringExtra("id");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        title=(TextView)findViewById(R.id.txtViewTitle);
        txtViewListingDetails=(TextView)findViewById(R.id.txtViewListingDetails);
        txtViewConsultationDetails=(TextView)findViewById(R.id.txtViewConsultationDetails);
        txtViewTime=(TextView)findViewById(R.id.txtViewTime);
        txtViewDate=(TextView)findViewById(R.id.txtViewDate);
        textViewMessage=(TextView)findViewById(R.id.textViewMessage);

        getConsultation();
        getListing();




    }

    public void displayDetails(){

        title.setText("Consultation ID : "+consultationId);
        txtViewListingDetails.setText("Listing ID : "+listing.getListingId()+"\nDescription : "+listing.getDescription()+"\n Location : "+listing.getDescription());
        txtViewConsultationDetails.setText("Consultation Details from Professional :");
        txtViewTime.setText("Proposed Time : "+consultation.getTime());
        txtViewDate.setText("Proposed Date : "+consultation.getDate());
        textViewMessage.setText("Message : "+consultation.getMessage());
    }
    public void getConsultation(){
        ref.child("Consultation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(consultationId)) {
                        consultation = child.getValue(Consultation.class);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }
    public void getListing() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
       DatabaseReference ref = database.getReference();

        ref.child("Listing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(consultation.getListingId())) {
                        listing = child.getValue(Listing.class);
                        displayDetails();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

    public void confirmConsultation(View v){
        ref.child("Consultation").child(consultationId).child("accepted").setValue(true);

        ref.child("Consultation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(consultationId)) {
                        consultation = child.getValue(Consultation.class);

                        ref.child("Listing").child(consultation.getListingId()).child("professionalId").setValue(consultation.getProfessionalId());
                        ref.child("Listing").child(consultation.getListingId()).child("active").setValue(false);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });

    }
    public void rescheduleConsultation(View v){

    }

}