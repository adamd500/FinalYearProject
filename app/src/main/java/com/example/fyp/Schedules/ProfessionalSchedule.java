package com.example.fyp.Schedules;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.fyp.CustomerFeatures.WelcomeCustomer;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ObjectClasses.Professional;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProfessionalSchedule extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private CalendarView calendarView;
    private FirebaseUser user;
    private String uid;
    private Calendar calendar;
    List<Job> jobs=new ArrayList<Job>();
    List<CalendarContract.EventDays> events=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_schedule);

        calendarView = findViewById(R.id.calendarView);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();


        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
       // populateConsultations();
        populateJobs();
    }

    public void populateJobs() {
        ref.child("Job").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Job job = child.getValue(Job.class);
                    if (job.getProfessionalId().equals(uid)) {

                        Intent intent= new Intent(Intent.ACTION_INSERT);
                        intent.setData(CalendarContract.CONTENT_URI);
                        intent.putExtra(CalendarContract.Events.TITLE,job.getJobTitle());
                        intent.putExtra(CalendarContract.Events.DESCRIPTION,job.getDescription());
                        intent.putExtra(CalendarContract.Events.EVENT_LOCATION,job.getLocation());
                        intent.putExtra(CalendarContract.Events.ALL_DAY,true);

                        if(intent.resolveActivity(getPackageManager())!=null){
                            startActivity(intent);
                        }else{
                            Toast.makeText(ProfessionalSchedule.this, "There is no app to perform", Toast.LENGTH_SHORT).show();

                        }
//                        calendar=Calendar.getInstance();
//                        String[]items=job.getStartDate().split("/");
//                        int year=Integer.parseInt(items[0]);
//                        int month=Integer.parseInt(items[1]);
//                        int day=Integer.parseInt(items[2]);
//                        calendar.add(1,1);
//                            calendar.set(year,month,day);
                          //  calendarView.set

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