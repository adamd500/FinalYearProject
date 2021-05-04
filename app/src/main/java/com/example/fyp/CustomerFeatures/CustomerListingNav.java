package com.example.fyp.CustomerFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.fyp.Messaging.InboxCustomer;
import com.example.fyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerListingNav extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_listing_nav);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(CustomerListingNav.this, InboxCustomer.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(CustomerListingNav.this, CustomerProfile.class);
                        startActivity(intent1);
                        return true;

                    case R.id.createListing:
                        Intent intent2 = new Intent(CustomerListingNav.this, CreateListing.class);
                        startActivity(intent2);
                        return true;

                    case R.id.myListings:
                     //   Intent intent3 = new Intent(CustomerListingNav.this, CustomerListingNav.class);
                       // startActivity(intent3);
                        return true;


                    case R.id.home:
                          Intent intent4 = new Intent(CustomerListingNav.this, WelcomeCustomer.class);
                        startActivity(intent4);
                }

                return false;
            }
        });
    }

    public void listingView(View v){
        Intent intent = new Intent(CustomerListingNav.this, CustomerAllListings.class);
        startActivity(intent);

    }

    public void viewConsultations(View v){
        Intent intent = new Intent(CustomerListingNav.this, CustomerAllConsultations.class);
        startActivity(intent);
    }
    public void viewJobsToBeFinalised(View v){
        Intent intent = new Intent(CustomerListingNav.this, JobsAwaitingFinalisation.class);
        startActivity(intent);
    }

    public void previousListings(View view) {
        Intent intent = new Intent(CustomerListingNav.this, PreviousListings.class);
        startActivity(intent);
    }
}