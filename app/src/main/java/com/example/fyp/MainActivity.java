package com.example.fyp;

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
import com.example.fyp.RegisterAndLogin.ProfessionalLogin;
import com.example.fyp.RegisterAndLogin.RegisterCustomer;
import com.example.fyp.RegisterAndLogin.RegisterProfessional;
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

        TextView textView = (TextView) findViewById(R.id.textView3);
        registerForContextMenu(textView);

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

                            Toast.makeText(MainActivity.this, "User signed in.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, WelcomeCustomer.class);
                            startActivity(intent);
                        } else {
                            Log.w("MySignIn", "SignInUserWithEmail : Failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public void professionalLogin(View v){
        Intent intent = new Intent(MainActivity.this, ProfessionalLogin.class);
        startActivity(intent);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.registerCus:
                Intent intent = new Intent(this, RegisterCustomer.class);
                startActivity(intent);
                return true;

            case R.id.registerProf:
                Intent intent1 = new Intent(this, RegisterProfessional.class);
                startActivity(intent1);
                return true;

            default:
                return super.onContextItemSelected(item);

        }
    }
}