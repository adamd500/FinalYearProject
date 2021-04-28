package com.example.fyp.CustomerFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.fyp.Messaging.InboxCustomer;
import com.example.fyp.ProfessionalFeatures.SetupStripe;
import com.example.fyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WelcomeCustomer extends AppCompatActivity {
   // private List<Listing> listings =new ArrayList<Listing>();
    EditText e1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomecustomer);

        e1=(EditText)findViewById(R.id.editTextEircode) ;

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(WelcomeCustomer.this, InboxCustomer.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(WelcomeCustomer.this, CustomerProfile.class);
                        startActivity(intent1);
                        return true;

                    case R.id.createListing:
                        Intent intent2 = new Intent(WelcomeCustomer.this, CreateListing.class);
                        startActivity(intent2);
                        return true;

                    case R.id.myListings:
                        Intent intent3 = new Intent(WelcomeCustomer.this, CustomerListingNav.class);
                        startActivity(intent3);
                        return true;


                    case R.id.home:
                     //   Intent intent4 = new Intent(SelectedListing.this, WelcomeCustomer.class);
                       // startActivity(intent4);
                }

                return false;
            }
        });
    }


    public void search(View view) {

        String keyword=e1.getText().toString();
        Intent intent = new Intent(this,JobsByKeyword.class);
        intent.putExtra("keyword",keyword);
        startActivity(intent);

    }
}