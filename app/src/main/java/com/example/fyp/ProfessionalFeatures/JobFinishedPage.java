package com.example.fyp.ProfessionalFeatures;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fyp.CustomerFeatures.WelcomeCustomer;
import com.example.fyp.R;

public class JobFinishedPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_finished_page);
    }


    public void home(View view) {
        Intent intent=new Intent(this, WelcomeProfessional.class);
        startActivity(intent);
    }
}