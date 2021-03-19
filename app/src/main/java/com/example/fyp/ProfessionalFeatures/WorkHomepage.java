package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WorkHomepage extends AppCompatActivity {
    private FirebaseUser user;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_homepage);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(WorkHomepage.this, InboxProfessional.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(WorkHomepage.this, ProfileHomePage.class);
                        startActivity(intent1);
                        return true;

                    case R.id.work:
//                        Intent intent2 = new Intent(WorkHomepage.this, WorkHomepage.class);
//                        startActivity(intent2);
                        return true;

                    case R.id.stats:
                        Intent intent3 = new Intent(WorkHomepage.this, WelcomeProfessional.class);
                        intent3.putExtra("professionalId",uid);
                        startActivity(intent3);
                        return true;

                    case R.id.home:
                        Intent intent4 = new Intent(WorkHomepage.this, ViewProfessionalFeedback.class);
                        startActivity(intent4);
                        return true;
                }

                return false;
            }
        });
    }

    public void getJobs(View v){
        Intent intent = new Intent(WorkHomepage.this, BrowseJobs.class);
        startActivity(intent);
    }

    public void findJobOnMap(View v){
        Intent intent = new Intent(WorkHomepage.this, BrowseJobsOnMap.class);
        startActivity(intent);
    }

    public void acceptedConsultations(View v){
        Intent intent = new Intent(WorkHomepage.this, AcceptedConsultations.class);
        startActivity(intent);
    }

    public void currentJobs(View v){
        Intent intent = new Intent(WorkHomepage.this, CurrentJobs.class);
        startActivity(intent);
    }

}