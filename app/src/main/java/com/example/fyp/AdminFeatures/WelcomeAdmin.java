package com.example.fyp.AdminFeatures;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.fyp.R;
import com.google.firebase.database.core.view.View;

public class WelcomeAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_admin);
    }



    public void verifyCustomers(android.view.View view) {
        Intent intent=new Intent(WelcomeAdmin.this,VerifyCustomers.class);
        startActivity(intent);
    }

    public void verifyProfessionals(android.view.View view) {
        Intent intent=new Intent(WelcomeAdmin.this,VerifyProfessionals.class);
        startActivity(intent);
    }
}