package com.example.fyp.CustomerFeatures;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Adapters.AdapterConsultationCustomer;
import com.example.fyp.AdminFeatures.SelectedProfessionalToVerify;
import com.example.fyp.Messaging.InboxCustomer;
import com.example.fyp.Notifications.ReminderConsultation;
import com.example.fyp.ObjectClasses.Consultation;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.ObjectClasses.Professional;
import com.example.fyp.ProfessionalFeatures.AcceptedConsultations;
import com.example.fyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

    private TextView title, txtViewTime, txtViewDate, textViewMessage;
    private String consultationId;
    private Consultation consultation;
    private Listing listing;
    private FirebaseDatabase database;
    private DatabaseReference ref, ref2;
    Professional professional;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_consultation);
        createNotificationChannel();

        Intent intent = getIntent();
        consultationId = intent.getStringExtra("id");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref2 = database.getReference();

        title = (TextView) findViewById(R.id.txtViewTitle);
        txtViewTime = (TextView) findViewById(R.id.txtViewTime);
        txtViewDate = (TextView) findViewById(R.id.txtViewDate);
        textViewMessage = (TextView) findViewById(R.id.textViewMessage);


        getConsultation();
        getListing();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(SelectedConsultation.this, InboxCustomer.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(SelectedConsultation.this, CustomerProfile.class);
                        startActivity(intent1);
                        return true;

                    case R.id.createListing:
                        Intent intent2 = new Intent(SelectedConsultation.this, CreateListing.class);
                        startActivity(intent2);
                        return true;

                    case R.id.myListings:
                        Intent intent3 = new Intent(SelectedConsultation.this, CustomerListingNav.class);
                        startActivity(intent3);
                        return true;


                    case R.id.home:
                        Intent intent4 = new Intent(SelectedConsultation.this, WelcomeCustomer.class);
                        startActivity(intent4);
                }

                return false;
            }
        });
    }

    public void displayDetails() {

        title.setText("Consultation \n Re Listing : "+listing.getTitle());
      //  txtViewListingDetails.setText("Listing Titled : " + listing.getTitle() + "\nDescription : " + listing.getDescription());
        txtViewTime.setText( consultation.getTime());
        txtViewDate.setText( consultation.getDate());
        textViewMessage.setText( consultation.getMessage());
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(consultationId)) {
                        consultation = child.getValue(Consultation.class);

                        CustomerAllConsultations.adapter.notifyDataSetChanged();
                        ref2.child("Listing").child(consultation.getListingId()).child("active").setValue(false);
                        ref2.child("Listing").child(consultation.getListingId()).child("professionalId").setValue(consultation.getProfessionalId());
                        ref2.child("Consultation").child(consultation.getConsultationId()).child("accepted").setValue(true);
                        CustomerAllConsultations.adapter.notifyDataSetChanged();

                        String[] parts = consultation.getTime().split("-");
                        String beginTime = parts[0];
                        String endTime = parts[1];

                        String[] startTime = beginTime.split(":");
                        String hour = startTime[0];
                        String minute = startTime[1];

                        String[] finishTime = endTime.split(":");
                        String hourEnd = finishTime[0];
                        String minuteEnd = finishTime[1];
//
                        String[] date = consultation.getDate().split("/");
                        int day = Integer.parseInt(date[0]);
                        int month = Integer.parseInt(date[1]);
                        int year = Integer.parseInt(date[2]);

                        Calendar beginCal = Calendar.getInstance();
                        beginCal.set(year, month, day, Integer.parseInt(hour), Integer.parseInt(minute));

                        Calendar finishCal = Calendar.getInstance();
                        finishCal.set(year, month, day, Integer.parseInt(hourEnd), Integer.parseInt(minuteEnd));

                        
                        Intent insertCalendarIntent = new Intent(Intent.ACTION_INSERT).setData(CalendarContract.Events.CONTENT_URI)
                                .putExtra(CalendarContract.Events.TITLE, "Professional Visiting for Consultation")
                                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginCal.getTimeInMillis()) // Only date part is considered when ALL_DAY is true; Same as DTSTART
                                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, finishCal.getTimeInMillis()) // Only date part is considered when ALL_DAY is true; Same as DTSTART
                                .putExtra(CalendarContract.Events.EVENT_LOCATION, consultation.getLocation())
                                .putExtra(CalendarContract.Events.DESCRIPTION, "Description :" + consultation.getMessage()) // Description
                                .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE)
                                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
                        startActivity(insertCalendarIntent);
                      //  Intent intent = new Intent(SelectedConsultation.this, WelcomeCustomer.class);
                        //tartActivity(intent);


                        Intent intent1 = new Intent(SelectedConsultation.this, ReminderConsultation.class);
                        PendingIntent pendingIntent= PendingIntent.getBroadcast(SelectedConsultation.this,0,intent1,0);
                        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                        long timeNow=System.currentTimeMillis();
                        long t=1000*60;
                        alarmManager.set(AlarmManager.RTC,timeNow+t,pendingIntent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel(){
        CharSequence name = "Consultation Reminder";
        String description = "Reminder of Consultation";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("notifyClient",name,importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void rescheduleConsultation(View v) {

        ref.child("Consultation").child(consultation.getConsultationId()).child("denied").setValue(true);
        Intent intent = new Intent(this, WelcomeCustomer.class);
        Toast.makeText(this, "Consultation Denied", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public void getProfessional(View v) {


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