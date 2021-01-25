package com.example.fyp.CustomerFeatures;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fyp.Messaging.InboxCustomer;
import com.example.fyp.ProfessionalFeatures.WelcomeProfessional;
import com.example.fyp.R;

public class WelcomeCustomer extends AppCompatActivity {
   // private List<Listing> listings =new ArrayList<Listing>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomecustomer);
    }

    public void createListing(View v){
        Intent intent = new Intent(WelcomeCustomer.this, CreateListing.class);
        startActivity(intent);
    }
    
    public void listingView(View v){
        Intent intent = new Intent(WelcomeCustomer.this, CustomerAllListings.class);
        startActivity(intent);

    }

    public void goToProfile(View v){
        Intent intent = new Intent(WelcomeCustomer.this, CustomerProfile.class);
        startActivity(intent);
    }
    public void viewConsultations(View v){
        Intent intent = new Intent(WelcomeCustomer.this, CustomerAllConsultations.class);
        startActivity(intent);
    }
    public void viewJobsToBeFinalised(View v){
        Intent intent = new Intent(WelcomeCustomer.this, JobsAwaitingFinalisation.class);
        startActivity(intent);
    }
    public void inboxCustomer(View v){
        Intent intent = new Intent(WelcomeCustomer.this, InboxCustomer.class);
        startActivity(intent);
    }
}