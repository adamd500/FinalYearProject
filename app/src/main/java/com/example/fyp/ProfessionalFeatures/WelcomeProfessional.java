package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuItem;
import android.view.View;

import com.example.fyp.CustomerFeatures.WelcomeCustomer;
import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeProfessional extends AppCompatActivity {
    private FirebaseUser user;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_professional);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch(item.getItemId()){
                   case R.id.inbox:
                       Intent intent = new Intent(WelcomeProfessional.this, InboxProfessional.class);
                       startActivity(intent);
                       return true;

                   case R.id.profile:
                       Intent intent1 = new Intent(WelcomeProfessional.this, ProfessionalProfile.class);
                       startActivity(intent1);
                       return true;

                   case R.id.work:
                       Intent intent2 = new Intent(WelcomeProfessional.this, BrowseJobs.class);
                       startActivity(intent2);
                       return true;

                   case R.id.stats:
                       Intent intent3 = new Intent(WelcomeProfessional.this, WelcomeProfessional.class);
                       intent3.putExtra("professionalId",uid);
                       startActivity(intent3);
                       return true;
               }

                return false;
            }
        });
    }

    public void getJobs(View v){
        Intent intent = new Intent(WelcomeProfessional.this, BrowseJobs.class);
        startActivity(intent);
    }

    public void findJobOnMap(View v){
        Intent intent = new Intent(WelcomeProfessional.this, BrowseJobsOnMap.class);
        startActivity(intent);
    }

    public void acceptedConsultations(View v){
        Intent intent = new Intent(WelcomeProfessional.this, AcceptedConsultations.class);
        startActivity(intent);
    }

    public void currentJobs(View v){
        Intent intent = new Intent(WelcomeProfessional.this, CurrentJobs.class);
        startActivity(intent);
    }

    public void inbox(View v){
        Intent intent = new Intent(WelcomeProfessional.this, InboxProfessional.class);
        startActivity(intent);
    }
    public void viewProfile(View v){
        Intent intent = new Intent(WelcomeProfessional.this, ProfessionalProfile.class);
        startActivity(intent);
    }

    public void covidInformation(View v){
        Intent intent = new Intent(WelcomeProfessional.this, CovidInformation.class);
        startActivity(intent);
    }

    public void feedback(View view) {

        Intent intent = new Intent(WelcomeProfessional.this, ViewProfessionalFeedback.class);
        intent.putExtra("professionalId",uid);
        startActivity(intent);

    }

    public void feedbackGraph(View view) {

        Intent intent = new Intent(WelcomeProfessional.this, FeedbackGraph.class);
        startActivity(intent);

    }

    public void setUpStripe(View v){
        Intent intent = new Intent(WelcomeProfessional.this, SetupStripe.class);
        startActivity(intent);
    }

    public void openCalendar(View v){

        Intent insertCalendarIntent = new Intent(Intent.ACTION_PICK).setData(CalendarContract.Events.CONTENT_URI);
        startActivity(insertCalendarIntent);

    }
}