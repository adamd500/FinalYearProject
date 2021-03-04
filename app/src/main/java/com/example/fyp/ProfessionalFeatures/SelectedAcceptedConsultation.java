package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.ObjectClasses.Consultation;
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

import java.util.Calendar;

public class SelectedAcceptedConsultation extends AppCompatActivity {

    private TextView title,txtViewListingDetails,txtViewConsultationDetails,txtViewTime,txtViewDate,textViewMessage;
    private String consultationId;
    private Consultation consultation;
    private Listing listing;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_accepted_consultation);

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

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(SelectedAcceptedConsultation.this, InboxProfessional.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(SelectedAcceptedConsultation.this, ProfessionalProfile.class);
                        startActivity(intent1);
                        return true;

                    case R.id.work:
                        Intent intent2 = new Intent(SelectedAcceptedConsultation.this, BrowseJobs.class);
                        startActivity(intent2);
                        return true;

                    case R.id.stats:
                        Intent intent3 = new Intent(SelectedAcceptedConsultation.this, ViewProfessionalFeedback.class);
                        intent3.putExtra("professionalId",uid);
                        startActivity(intent3);
                        return true;

                    case R.id.home:
                        Intent intent4 = new Intent(SelectedAcceptedConsultation.this, WelcomeProfessional.class);
                        startActivity(intent4);
                }

                return false;
            }
        });
    }

    public void completeConsultation(View v){
       // notifyDataSetChanged();
        Intent intent= new Intent(this, CreateJob.class);
        intent.putExtra("id",consultationId);
        startActivity(intent);

        AcceptedConsultations.myAdapter.notifyDataSetChanged();
        ref.child("Consultation").child(consultationId).child("finished").setValue(true);
    }

    public void displayDetails(){

        title.setText("Consultation ID : "+consultationId);
        txtViewListingDetails.setText("Listing Title : "+listing.getTitle()+"\nDescription : "+listing.getDescription()+"\n Location : "+listing.getLocation());
        txtViewConsultationDetails.setText("Your Consultation Details :");
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

    public void addToCalendar(View view) {

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
                                .putExtra(CalendarContract.Events.TITLE,"Visiting Customer for Consultation")
                                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,beginCal.getTimeInMillis()) // Only date part is considered when ALL_DAY is true; Same as DTSTART
                                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,finishCal.getTimeInMillis()) // Only date part is considered when ALL_DAY is true; Same as DTSTART
                                .putExtra(CalendarContract.Events.EVENT_LOCATION, consultation.getLocation())
                                .putExtra(CalendarContract.Events.DESCRIPTION, "Description :"+ consultation.getMessage()) // Description
                                .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE)
                                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);

                        startActivity(insertCalendarIntent);
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