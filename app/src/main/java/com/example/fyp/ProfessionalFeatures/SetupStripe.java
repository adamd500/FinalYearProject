package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.ObjectClasses.Professional;
import com.example.fyp.ProfessionalFeatures.StripeDetails;
import com.example.fyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.AccountLink;
import com.stripe.model.LoginLink;

import java.util.HashMap;
import java.util.Map;

public class SetupStripe extends AppCompatActivity {
    private FirebaseUser user;
    private String uid;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String accountID;
    Thread thread, thread2;
    TextView tv;
    String url,loginUrl;
    LoginLink login_link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_stripe);
        Stripe.apiKey = "sk_test_51IFKQvKgTR7yUeIexy2I5Th0pv3OGDiM088vBZY2YFHLkJO1uxZrCesiQoGzUewSLdkwnkcETWhlwk5bGlUCsrLB00FF8KPznk";

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        tv = (TextView) findViewById(R.id.tv);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("account", accountID);
                params.put(
                        "refresh_url",
                        "https://dashboard.stripe.com/"+accountID+"/test/dashboard"
                );
                params.put(
                        "return_url",
                        "https://example.com/return"
                );
                params.put("type", "account_onboarding");
                try {
                    AccountLink accountLink =
                            null;
                    try {
                        accountLink = AccountLink.create(params);
                    } catch (StripeException e) {
                        e.printStackTrace();
                    }
                    url = accountLink.getUrl();

                tv.setText("ACCount url " + url);

                Intent intent = new Intent(getApplicationContext(), StripeDetails.class);
                intent.putExtra("url",url);

                startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        getCustomer();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(SetupStripe.this, InboxProfessional.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(SetupStripe.this, ProfessionalProfile.class);
                        startActivity(intent1);
                        return true;

                    case R.id.work:
                        Intent intent2 = new Intent(SetupStripe.this, BrowseJobs.class);
                        startActivity(intent2);
                        return true;

                    case R.id.stats:
                        Intent intent3 = new Intent(SetupStripe.this, ViewProfessionalFeedback.class);
                        intent3.putExtra("professionalId",uid);
                        startActivity(intent3);
                        return true;

                    case R.id.home:
                        Intent intent4 = new Intent(SetupStripe.this, WelcomeProfessional.class);
                        startActivity(intent4);
                }

                return false;
            }
        });
    }
    public void getCustomer(){
        ref.child("Professional").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(uid)) {
                        Professional customer = child.getValue(Professional.class);
                        accountID=customer.getStripeKey();
                        thread.start();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

}







