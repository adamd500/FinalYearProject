package com.example.fyp.CustomerFeatures;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Adapters.AdapterConsultationCustomer;
import com.example.fyp.AdminFeatures.SelectedProfessionalToVerify;
import com.example.fyp.ObjectClasses.Consultation;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.ObjectClasses.Professional;
import com.example.fyp.ProfessionalFeatures.AcceptedConsultations;
import com.example.fyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class SelectedConsultation extends AppCompatActivity {

    private TextView title, txtViewListingDetails, txtViewConsultationDetails, txtViewTime, txtViewDate, textViewMessage;
    private String consultationId;
    private Consultation consultation;
    private Listing listing;
    private FirebaseDatabase database;
    private DatabaseReference ref, ref2;
    Professional professional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_consultation);

        Intent intent = getIntent();
        consultationId = intent.getStringExtra("id");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref2 = database.getReference();

        title = (TextView) findViewById(R.id.txtViewTitle);
        txtViewListingDetails = (TextView) findViewById(R.id.txtViewListingDetails);
        txtViewConsultationDetails = (TextView) findViewById(R.id.txtViewConsultationDetails);
        txtViewTime = (TextView) findViewById(R.id.txtViewTime);
        txtViewDate = (TextView) findViewById(R.id.txtViewDate);
        textViewMessage = (TextView) findViewById(R.id.textViewMessage);

        getConsultation();
        getListing();


        // CustomerAllConsultations.adapter.notifyDataSetChanged();


    }

    public void displayDetails() {

        title.setText("Consultation ID : " + consultationId);
        txtViewListingDetails.setText("Listing ID : " + listing.getListingId() + "\nDescription : " + listing.getDescription() + "\n Location : " + listing.getDescription());
        txtViewConsultationDetails.setText("Consultation Details from Professional :");
        txtViewTime.setText("Proposed Time : " + consultation.getTime());
        txtViewDate.setText("Proposed Date : " + consultation.getDate());
        textViewMessage.setText("Message : " + consultation.getMessage());
    }

    public void getConsultation() {
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

    public void confirmConsultation(String profEmail) {
        //ref.child("Consultation").child(consultationId).child("accepted").setValue(true);

        ref.child("Consultation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(consultationId)) {
                        consultation = child.getValue(Consultation.class);

                        String[]parts=consultation.getTime().split("-");
                        String beginTime=parts[0];
                        String endTime=parts[1];

                        String[]startTime=beginTime.split(":");
                        String hour=startTime[0];
                        String minute=startTime[1];

                        String[]finishTime=endTime.split(":");
                        String hourEnd=finishTime[0];
                        String minuteEnd=finishTime[1];
//
                        String[] date=consultation.getDate().split("/");
                        int day=Integer.parseInt(date[0]);
                        int month =Integer.parseInt(date[1]);
                        int year=Integer.parseInt(date[2]);

                        Calendar beginCal = Calendar.getInstance();
                        beginCal.set(year, month,day,Integer.parseInt(hour),Integer.parseInt(minute));

                        Calendar finishCal = Calendar.getInstance();
                        finishCal.set(year, month,day,Integer.parseInt(hourEnd),Integer.parseInt(minuteEnd));

                        Intent insertCalendarIntent = new Intent(Intent.ACTION_INSERT).setData(CalendarContract.Events.CONTENT_URI)
                                .putExtra(CalendarContract.Events.TITLE,"Professional Visiting for Consultation")
                                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,beginCal.getTimeInMillis()) // Only date part is considered when ALL_DAY is true; Same as DTSTART
                                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,finishCal.getTimeInMillis()) // Only date part is considered when ALL_DAY is true; Same as DTSTART
                                .putExtra(CalendarContract.Events.EVENT_LOCATION, consultation.getLocation())
                                .putExtra(CalendarContract.Events.DESCRIPTION, "Description :"+ consultation.getMessage()) // Description
                                .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE)
                                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);

                        startActivity(insertCalendarIntent);
                         CustomerAllConsultations.adapter.notifyDataSetChanged();
                          ref.child("Listing").child(consultation.getListingId()).child("active").setValue(false);
                         ref.child("Listing").child(consultation.getListingId()).child("professionalId").setValue(consultation.getProfessionalId());

                        Intent intent=new Intent(SelectedConsultation.this,WelcomeCustomer.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });

    }

    public void rescheduleConsultation(View v) {

        ref.child("Consultation").child(consultation.getConsultationId()).child("denied").setValue(true);
        Intent intent=new Intent(this,WelcomeCustomer.class);
        Toast.makeText(this, "Consultation Denied", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public void getProfessional( View v) {


        ref2.child("Professional").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(consultation.getProfessionalId())) {
                        professional = child.getValue(Professional.class);

                        confirmConsultation(professional.getEmail());

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