package com.example.fyp.RegisterAndLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.ProfessionalFeatures.WelcomeProfessional;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfessionalLogin extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_login);

        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(View v) {

        EditText emailTxt = (EditText) findViewById(R.id.emailEditText);
        EditText passwordTxt = (EditText) findViewById(R.id.passwordEditText);

        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(ProfessionalLogin.this, "User signed in.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProfessionalLogin.this, WelcomeProfessional.class);
                            startActivity(intent);
                        } else {
                            Log.w("MySignIn", "SignInUserWithEmail : Failure", task.getException());
                            Toast.makeText(ProfessionalLogin.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
    public void registerProfessional(View v){
        Intent intent = new Intent(this, RegisterProfessional.class);
        startActivity(intent);
    }
}