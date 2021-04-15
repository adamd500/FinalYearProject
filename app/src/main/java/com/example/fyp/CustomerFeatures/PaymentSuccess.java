package com.example.fyp.CustomerFeatures;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fyp.R;

public class PaymentSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);
    }

    public void home(View view) {
        Intent intent=new Intent(this,WelcomeCustomer.class);
        startActivity(intent);
    }
}