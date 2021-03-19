package com.example.fyp.WebLinks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.ProfessionalFeatures.ProfessionalProfile;
import com.example.fyp.ProfessionalFeatures.ProfileHomePage;
import com.example.fyp.ProfessionalFeatures.ViewProfessionalFeedback;
import com.example.fyp.ProfessionalFeatures.WelcomeProfessional;
import com.example.fyp.ProfessionalFeatures.WorkHomepage;
import com.example.fyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CovidInformation extends AppCompatActivity {

    WebView webview;
    private FirebaseUser user;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_information);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        webview = findViewById(R.id.webView);
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl("https://www.citizensinformation.ie/en/employment/employment_rights_during_covid19_restrictions.html");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(CovidInformation.this, InboxProfessional.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(CovidInformation.this, ProfileHomePage.class);
                        startActivity(intent1);
                        return true;

                    case R.id.work:
                        Intent intent2 = new Intent(CovidInformation.this, WorkHomepage.class);
                        startActivity(intent2);
                        return true;

                    case R.id.stats:
                        Intent intent3 = new Intent(CovidInformation.this, ViewProfessionalFeedback.class);
                        intent3.putExtra("professionalId",uid);
                        startActivity(intent3);
                        return true;

                    case R.id.home:
                        Intent intent4 = new Intent(CovidInformation.this, WelcomeProfessional.class);
                        startActivity(intent4);
                }

                return false;
            }
        });
    }
}