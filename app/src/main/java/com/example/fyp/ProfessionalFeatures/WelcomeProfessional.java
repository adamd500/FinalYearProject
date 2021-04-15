package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.R;
import com.example.fyp.WebLinks.CovidInformation;
import com.example.fyp.WebLinks.TrafficLink;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WelcomeProfessional extends AppCompatActivity {
    private FirebaseUser user;
    private String uid;
    TextView tv,tv2;
    String text;
    String next;

    Thread thread;
    String allTraffic;
    String nationalTraffic,dublinTraffic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_professional);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        tv = findViewById(R.id.nationwideTraffic);
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv2 = findViewById(R.id.dublinTraffic);
        tv2.setMovementMethod(new ScrollingMovementMethod());

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect("https://www.rte.ie/traffic/").get();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Elements divs = doc.getElementsByTag("div");
                Elements divTags = doc.getElementsByTag("div");

                for (Element elem : divTags) {
                    if (elem.attr("class").equalsIgnoreCase("summary_tab_content")) {
                        if(elem.attr("ng-show").equalsIgnoreCase("isSummTabSet(1)")) {

                            text = text + elem.text();
                            String[]sections=text.toString().split("NATIONAL TRAFFIC");
                            allTraffic=sections[1];

                            String nextSections[]=allTraffic.toString().split("DUBLIN TRAFFIC | NATIONAL TRAFFIC");
                            if(nextSections[0]!=null) {
                                nationalTraffic = nextSections[0];
                                nationalTraffic=nationalTraffic.replaceAll("\\.\\s?","\\.\n");

                            }
                            if( nextSections.length>=2) {
                                 next = nextSections[1];
                                String dublinTrafficSection[]=next.toString().split("PUBLIC TRANSPORT | ROADWORKS");
                                dublinTraffic=dublinTrafficSection[0];
                                dublinTraffic=dublinTraffic.replaceAll("\\.\\s?","\\.\n");

                            }

                        }
                    }
                }
            }


        });
        thread.start();
        try {
            thread.join();
            tv.setText(nationalTraffic);
            tv2.setText(dublinTraffic);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch(item.getItemId()){
                   case R.id.inbox:
                       Intent intent = new Intent(WelcomeProfessional.this, InboxProfessional.class);
                       startActivity(intent);
                       return true;

                   case R.id.profile:
                       Intent intent1 = new Intent(WelcomeProfessional.this, ProfileHomePage.class);
                       startActivity(intent1);
                       return true;

                   case R.id.work:
                       Intent intent2 = new Intent(WelcomeProfessional.this, WorkHomepage.class);
                       startActivity(intent2);
                       return true;

                   case R.id.stats:
                       Intent intent3 = new Intent(WelcomeProfessional.this, ViewProfessionalFeedback.class);
                       intent3.putExtra("professionalId",uid);
                       startActivity(intent3);
                       return true;
               }

                return false;
            }
        });
    }



    public void inbox1(View v){
        Intent intent = new Intent(WelcomeProfessional.this, InboxProfessional.class);
        startActivity(intent);
    }

    public void jobf(View view) {

        Intent intent = new Intent(WelcomeProfessional.this, BrowseJobs.class);
        startActivity(intent);

    }


    public void openCalendar(View v){

        Intent intent = new Intent();
       ComponentName cn= new ComponentName("com.google.android.calendar","com.android.calendar.LaunchActivity");
        intent.setComponent(cn);
        startActivity(intent);


    }


    public void traffic(View view) {
        Intent intent = new Intent(WelcomeProfessional.this, TrafficLink.class);
        startActivity(intent);
    }
}