package com.example.fyp.RegisterAndLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ObjectClasses.Professional;
import com.example.fyp.ProfessionalFeatures.WelcomeProfessional;
import com.example.fyp.R;
import com.example.fyp.CustomerFeatures.WelcomeCustomer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.param.AccountCreateParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterProfessional extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase,ref;
    private FirebaseAnalytics analytics;
    private static final String USER = "Professional";
    private static final String TAG = "RegisterProfessional";
    private Professional professional;
    Account account;
    Thread thread;
    String uid;
    String address;
    String dob;
    String location;
    String number;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_professional);

        analytics=FirebaseAnalytics.getInstance(this);

        Bundle bundle= new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME,"Professional Register Page");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS,"RegisterProfessional");
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW,bundle);

        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference(USER);
        ref=database.getReference();

        mAuth=FirebaseAuth.getInstance();

        Stripe.apiKey = "sk_test_51IFKQvKgTR7yUeIexy2I5Th0pv3OGDiM088vBZY2YFHLkJO1uxZrCesiQoGzUewSLdkwnkcETWhlwk5bGlUCsrLB00FF8KPznk";

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                AccountCreateParams params =
                        AccountCreateParams.builder()
                                .setCountry("IE")
                                .setEmail(email)
                                .setType(AccountCreateParams.Type.EXPRESS)
                                .setBusinessType(AccountCreateParams.BusinessType.INDIVIDUAL)
                                .setCapabilities(
                                        AccountCreateParams.Capabilities.builder()
                                                .setCardPayments(
                                                        AccountCreateParams.Capabilities.CardPayments.builder()
                                                                .setRequested(true)
                                                                .build())
                                                .setTransfers(
                                                        AccountCreateParams.Capabilities.Transfers.builder()
                                                                .setRequested(true)
                                                                .build())
                                                .build())

                                .build();

                try {
                    account = Account.create(params);

                    ref.child("Professional").child(uid).child("stripeKey").setValue(account.getId());
                    goToWelcomePage();

                } catch (StripeException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public void createProfessional(View v) {
        EditText nameTxt = (EditText) findViewById(R.id.name);
        EditText dobTxt = (EditText) findViewById(R.id.dob);
        EditText addressTxt = (EditText) findViewById(R.id.address);
        EditText locationTxt = (EditText) findViewById(R.id.location);
        EditText numberTxt = (EditText) findViewById(R.id.number);
        EditText emailTxt = (EditText) findViewById(R.id.email);
        EditText passwordTxt = (EditText) findViewById(R.id.password);
        EditText passwordTxt2 = (EditText) findViewById(R.id.password2);
        EditText tradetxt=(EditText)findViewById(R.id.trade);


         email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        String password2=passwordTxt2.getText().toString();
         dob = dobTxt.getText().toString();
        String address = addressTxt.getText().toString();
        String location = locationTxt.getText().toString();
        String number = numberTxt.getText().toString();
        String name = nameTxt.getText().toString();
        String trade = tradetxt.getText().toString();

        List<String> feedback = new ArrayList<String>();
        String s="s";
        feedback.add(s);
        List<Job>jobsCompleted=new ArrayList<Job>();


        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(address) || TextUtils.isEmpty(location)
                || TextUtils.isEmpty(number) || TextUtils.isEmpty(name)|| TextUtils.isEmpty(name)|| TextUtils.isEmpty(password2)||!password.equals(password2)) {
            Toast.makeText(getApplicationContext(), "Please ensure all fields are completed and that passwords match",
                    Toast.LENGTH_SHORT).show();
            return;
        }else {

           // registerStripe();
            professional = new Professional( name,  dob,  address,  false, feedback,  location,  number,  email,  password,  "username", "selife","id",
                    false,
                    false,  trade,0,  "s",  "s",
                    0, 0, 0, 0, 0,0,"stripe") ;



            registerProfessional(email, password);

        }
    }
    public void registerProfessional(String email,String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"Create user with email : success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            Log.w(TAG,"Create user with email : failure",task.getException());
                            Toast.makeText(RegisterProfessional.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser currentUser){
         uid=currentUser.getUid();
        professional.setUsername(uid);
        mDatabase.child(uid).setValue(professional);
        thread.start();


    }
    public void goToWelcomePage(){
        Intent welcomeIntent = new Intent(this, WelcomeProfessional.class);

        startActivity(welcomeIntent);
    }
}