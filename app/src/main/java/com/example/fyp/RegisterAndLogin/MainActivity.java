package com.example.fyp.RegisterAndLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.CustomerFeatures.WelcomeCustomer;
import com.example.fyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void customerLogin(View v){
        Intent intent=new Intent(this,CustomerLogin.class);
        startActivity(intent);
    }
    public void professionalLogin(View v){
        Intent intent=new Intent(this,ProfessionalLogin.class);
        startActivity(intent);
    }
    public void adminLogin(View v){
        Intent intent=new Intent(this,AdminLogin.class);
        startActivity(intent);
    }




}