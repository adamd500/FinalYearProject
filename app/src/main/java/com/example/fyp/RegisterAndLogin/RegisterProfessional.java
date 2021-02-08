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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterProfessional extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;

    private static final String USER = "Professional";
    private static final String TAG = "RegisterProfessional";
    private Professional professional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_professional);

        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference(USER);
        mAuth=FirebaseAuth.getInstance();


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
        EditText worktxt=(EditText)findViewById(R.id.workRadius);
        EditText username = (EditText) findViewById(R.id.username);


        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        String password2=passwordTxt2.getText().toString();
        String dob = dobTxt.getText().toString();
        String address = addressTxt.getText().toString();
        String location = locationTxt.getText().toString();
        String number = numberTxt.getText().toString();
        String name = nameTxt.getText().toString();
        String trade = tradetxt.getText().toString();
        String workRadius = worktxt.getText().toString();
        int workRadiusInt = Integer.parseInt(workRadius);
        String usernamStr=username.getText().toString();

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
            professional = new Professional( name,  dob,  address,  false, feedback,  location,  number,  email,  password,  "username",  false,
                    false,  trade,  workRadiusInt, "s", "s", "s", "s",
                    0, 0, 0, 0, 0,0) ;


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
    public void registerStripe(){
//        Stripe.apiKey = "sk_test_51IFKQvKgTR7yUeIexy2I5Th0pv3OGDiM088vBZY2YFHLkJO1uxZrCesiQoGzUewSLdkwnkcETWhlwk5bGlUCsrLB00FF8KPznk";
//        Map<String, Object> cardPayments =
//                new HashMap<>();
//        cardPayments.put("requested", true);
//        Map<String, Object> transfers = new HashMap<>();
//        transfers.put("requested", true);
//        Map<String, Object> capabilities =
//                new HashMap<>();
//        capabilities.put("card_payments", cardPayments);
//        capabilities.put("transfers", transfers);
//        Map<String, Object> params = new HashMap<>();
//        params.put("type", "custom");
//        params.put("country", "US");
//        params.put("email", "jenny.rosen@example.com");
//        params.put("capabilities", capabilities);
//
//        Account account = Account.create(params);
    }
    public void updateUI(FirebaseUser currentUser){
        String uid=currentUser.getUid();
        professional.setUsername(uid);
        mDatabase.child(uid).setValue(professional);
     //   professional.setProfessionalId(keyId);
        Intent welcomeIntent = new Intent(this, WelcomeProfessional.class);

        startActivity(welcomeIntent);
    }
}