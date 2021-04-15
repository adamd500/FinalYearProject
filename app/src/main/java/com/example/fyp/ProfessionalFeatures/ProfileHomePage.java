package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.R;
import com.example.fyp.WebLinks.BecomingSelfEmployedInfo;
import com.example.fyp.WebLinks.CovidInformation;
import com.example.fyp.WebLinks.EmploymentStatusInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

public class ProfileHomePage extends AppCompatActivity {
    private FirebaseUser user;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prof_profile_homepage);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(getApplicationContext(), InboxProfessional.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                       // Intent intent1 = new Intent(getApplicationContext(), ProfileHomePage.class);
                        //startActivity(intent1);
                        return true;

                    case R.id.work:
                        Intent intent2 = new Intent(getApplicationContext(), WorkHomepage.class);
                        startActivity(intent2);
                        return true;

                    case R.id.stats:
                        Intent intent3 = new Intent(getApplicationContext(), ViewProfessionalFeedback.class);
                        intent3.putExtra("professionalId",uid);
                        startActivity(intent3);
                        return true;

                    case R.id.home:
                        Intent intent4 = new Intent(getApplicationContext(), WelcomeProfessional.class);
                        startActivity(intent4);
                }

                return false;
            }
        });
    }

    public void selfEmployed(View view) {
        Intent intent = new Intent(getApplicationContext(), BecomingSelfEmployedInfo.class);
        startActivity(intent);
    }

    public void employmentStatus(View view) {
        Intent intent = new Intent(getApplicationContext(), EmploymentStatusInfo.class);
        startActivity(intent);
    }

    public void covidInfo(View view) {
        Intent intent = new Intent(getApplicationContext(), CovidInformation.class);
        startActivity(intent);
    }

    public void details(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfessionalProfile.class);
        startActivity(intent);
    }

    public void graph(View view) {
        Intent intent = new Intent(getApplicationContext(), Graph2.class);
        startActivity(intent);
    }
}