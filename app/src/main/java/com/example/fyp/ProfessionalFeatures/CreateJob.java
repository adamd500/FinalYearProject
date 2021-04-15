package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.ObjectClasses.Consultation;
import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.ObjectClasses.Job;
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

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateJob extends AppCompatActivity {

    private String consultationId;
    Consultation consultation;
    Listing listing;
    private FirebaseDatabase database;
    private DatabaseReference ref, ref2;
    private EditText e1, e2, e3, e4, e6;
    TextView startDate;
    TextView loc;
    private FirebaseUser user;
    private String uid;
    private static final String Job = "Job";
    DatePickerDialog.OnDateSetListener dateSetListener;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref2 = database.getReference(Job);

        Intent intent = getIntent();
        consultationId = intent.getStringExtra("id");

        loc = (TextView) findViewById(R.id.location);
        e2 = (EditText) findViewById(R.id.duration);
        e3 = (EditText) findViewById(R.id.description);
        e4 = (EditText) findViewById(R.id.quote);
        e6 = (EditText) findViewById(R.id.jobTitle);

        spinner=(Spinner)findViewById(R.id.spinner);

        startDate = (TextView) findViewById(R.id.startDate);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CreateJob.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, dateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
                dialog.show();
                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth1) {
                        month1 = month1 + 1;

                        String dateStr = dayOfMonth1 + "/" + month1 + "/" + year1;
                        startDate.setText(dateStr);

                    }
                };
            }
    });

    user =FirebaseAuth.getInstance().

    getCurrentUser();

    uid =user.getUid();

    getConsultation();

    getListing();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(CreateJob.this, InboxProfessional.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(CreateJob.this, ProfileHomePage.class);
                        startActivity(intent1);
                        return true;

                    case R.id.work:
                        Intent intent2 = new Intent(CreateJob.this, WorkHomepage.class);
                        startActivity(intent2);
                        return true;

                    case R.id.stats:
                        Intent intent3 = new Intent(CreateJob.this, ViewProfessionalFeedback.class);
                        intent3.putExtra("professionalId",uid);
                        startActivity(intent3);
                        return true;

                    case R.id.home:
                        Intent intent4 = new Intent(CreateJob.this, WelcomeProfessional.class);
                        startActivity(intent4);
                }

                return false;
            }
        });
}

    public void getConsultation() {
        ref.child("Consultation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(consultationId)) {
                        consultation = child.getValue(Consultation.class);
                        // loc.setText("Location"+consultation.getLocation());
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
        ref.child("Listing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(consultation.getListingId())) {
                        listing = child.getValue(Listing.class);
                        loc.setText(listing.getLocation());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

    public void getCustomer(View v) {
        ref.child("Listing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(consultation.getListingId())) {
                        listing = child.getValue(Listing.class);

                    //    createJob(listing.getCustomerUsername());

                        ref.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Iterable<DataSnapshot> children = snapshot.getChildren();
                                for (DataSnapshot child : children) {

                                    Customer customer = child.getValue(Customer.class);
                                    if (customer.getUsername().equals(listing.getCustomerUsername())) {

                                        createJob(customer.getEmail());


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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

    public void createJob(String customerEmail) {

        ref.child("Consultation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(consultationId)) {
                        consultation = child.getValue(Consultation.class);
                        // loc.setText("Location"+consultation.getLocation());

                        String jobId = ref2.push().getKey();

                        String duration = e2.getText().toString();
                        String description = e3.getText().toString();
                        int quote = Integer.parseInt(e4.getText().toString());
                        String start = startDate.getText().toString();
                        String title = e6.getText().toString();

                        Job job = new Job(jobId, consultation.getCustomerId(), uid, consultation, listing, 0, loc.getText().toString(), duration,
                                description, quote, "t", "t", false, start, "s", "s", "s", title);

                        String[] date = job.getStartDate().split("/");
                        int day = Integer.parseInt(date[0]);
                        int month = Integer.parseInt(date[1]);
                        int year = Integer.parseInt(date[2]);

                        Calendar beginCal = Calendar.getInstance();
                        beginCal.set(year, month, day);

                        Calendar finishCal = Calendar.getInstance();
                        finishCal.set(year, month, day);
                        finishCal.add(Calendar.DAY_OF_YEAR, Integer.parseInt(duration));

                        ref2.child(jobId).setValue(job);

                        ref2.child(jobId).child("estimatedDuration").setValue(duration+" "+spinner.getSelectedItem());

//
//                        Intent insertCalendarIntent = new Intent(Intent.ACTION_INSERT).setData(CalendarContract.Events.CONTENT_URI)
//                                .putExtra(CalendarContract.Events.TITLE, "Working on Job Titled :" + job.getJobTitle())
//                                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
//                                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginCal.getTimeInMillis())
//                                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, finishCal.getTimeInMillis())
//                                .putExtra(CalendarContract.Events.EVENT_LOCATION, job.getLocation())
//                                .putExtra(Intent.EXTRA_EMAIL, customerEmail)
//                                .putExtra(CalendarContract.Events.DESCRIPTION, "Description :" + job.getDescription()) // Description
//                                .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE)
//                                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
//
//                        startActivity(insertCalendarIntent);

                        Toast.makeText(CreateJob.this, "Job has been created", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),WelcomeProfessional.class);
                        startActivity(intent);
                        //  backToHomePage();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });


    }

    private void createNotificationChannel(){

    }
}