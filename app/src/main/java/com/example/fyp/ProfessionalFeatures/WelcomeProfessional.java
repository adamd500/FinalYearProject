package com.example.fyp.ProfessionalFeatures;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.R;

public class WelcomeProfessional extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_professional);
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

    //public void
}