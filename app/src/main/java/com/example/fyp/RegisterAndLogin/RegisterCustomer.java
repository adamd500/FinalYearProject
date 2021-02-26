package com.example.fyp.RegisterAndLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.R;
import com.example.fyp.CustomerFeatures.WelcomeCustomer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.Address;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountUpdateParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class RegisterCustomer extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;

    private static final String USER = "Customer";
    private static final String TAG = "RegisterCustomer";
    private Customer customer;
    String email;
    Account account;
    Thread thread;
    String uid;
    String address;
    String dob;
    String location;
    String number;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer);

        //StrictMode.ThreadPolicy.Builder().;

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(USER);
        mAuth = FirebaseAuth.getInstance();

    }
    public void createCustomer (View v) throws StripeException {

        EditText nameTxt = (EditText) findViewById(R.id.name);
        EditText dobTxt = (EditText) findViewById(R.id.dob);
        EditText addressTxt = (EditText) findViewById(R.id.address);
        EditText locationTxt = (EditText) findViewById(R.id.location);
        EditText numberTxt = (EditText) findViewById(R.id.number);
        EditText emailTxt = (EditText) findViewById(R.id.email);
        EditText passwordTxt = (EditText) findViewById(R.id.password);
        EditText passwordTxt2 = (EditText) findViewById(R.id.password2);


        email = emailTxt.getText().toString();
      //  stripeSetup();

        String password = passwordTxt.getText().toString();
        String password2 = passwordTxt2.getText().toString();
         dob = dobTxt.getText().toString();
         address = addressTxt.getText().toString();
         location = locationTxt.getText().toString();
         number = numberTxt.getText().toString();
         name = nameTxt.getText().toString();

        List<String> feedback = new ArrayList<String>();



        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(address) || TextUtils.isEmpty(location)
                || TextUtils.isEmpty(number) || TextUtils.isEmpty(name) || TextUtils.isEmpty(name) || TextUtils.isEmpty(password2) || !password.equals(password2)) {
            Toast.makeText(getApplicationContext(), "Please ensure all fields are completed and that passwords match",
                    Toast.LENGTH_SHORT).show();
            //    return;
        } else {
            customer = new Customer(name, dob, address, false, feedback, location, number, email, password, "usernamStr", "s", "s", "stripe");
            customerProfessional(email, password);

        }
    }
    public void customerProfessional(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Create user with email : success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "Create user with email : failure", task.getException());
                            Toast.makeText(RegisterCustomer.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser currentUser) {
          uid = currentUser.getUid();
        customer.setUsername(uid);
      //  customer.setStripeKey(account.getId());
        mDatabase.child(uid).setValue(customer);
        thread.start();

        Intent welcomeIntent = new Intent(this, WelcomeCustomer.class);
        startActivity(welcomeIntent);
    }
}