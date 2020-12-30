package com.example.fyp.ProfessionalFeatures;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
}