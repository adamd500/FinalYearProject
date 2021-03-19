package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fyp.DocumentUploading.CustomerUploadIdDocument;
import com.example.fyp.DocumentUploading.CustomerUploadSelfieImage;
import com.example.fyp.DocumentUploading.ProfessionalUploadGardaVet;
import com.example.fyp.DocumentUploading.ProfessionalUploadId;
import com.example.fyp.DocumentUploading.ProfessionalUploadSafePass;
import com.example.fyp.DocumentUploading.ProfessionalUploadSelfie;
import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.ObjectClasses.Professional;
import com.example.fyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfessionalProfile extends AppCompatActivity {
    private TextView t1, t2, t3, t4, t5, t6, t7, t8, t9;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private String uid;
    private Professional professional;
    private Button buttonID, buttonSelfie,buttonSafePass,buttonGardaVet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_profile);

        t1 = (TextView) findViewById(R.id.textViewAddress);
        t2 = (TextView) findViewById(R.id.textViewDob);
        t3 = (TextView) findViewById(R.id.textViewEmail);
        t4 = (TextView) findViewById(R.id.textViewIdVer);
        t5 = (TextView) findViewById(R.id.textViewLocation);
        t6 = (TextView) findViewById(R.id.textViewName);
        t7 = (TextView) findViewById(R.id.textViewPassword);
        t8 = (TextView) findViewById(R.id.textViewNumber);
        t9 = (TextView) findViewById(R.id.textViewIdRequired);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(ProfessionalProfile.this, InboxProfessional.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(ProfessionalProfile.this, ProfileHomePage.class);
                        startActivity(intent1);
                        return true;

                    case R.id.work:
                        Intent intent2 = new Intent(ProfessionalProfile.this, WorkHomepage.class);
                        startActivity(intent2);
                        return true;

                    case R.id.stats:
                        Intent intent3 = new Intent(ProfessionalProfile.this, ViewProfessionalFeedback.class);
                        intent3.putExtra("professionalId",uid);
                        startActivity(intent3);
                        return true;

                    case R.id.home:
                        Intent intent4 = new Intent(ProfessionalProfile.this, WelcomeProfessional.class);
                        startActivity(intent4);
                }

                return false;
            }
        });

        ref.child("Professional").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(uid)) {
                        professional = child.getValue(Professional.class);
                        t1.setText("Address:     " + professional.getAddress());
                        t2.setText("Date of Birth:      " + professional.getDob());
                        t3.setText("Email:      " + professional.getEmail());
                        t4.setText("Id Verified:      " + professional.isIdVerified());
                        t5.setText("Location:     " + professional.getLocation());
                        t6.setText(professional.getName());
                    //    t7.setText("ID Verified: "+professional);
                        t7.setText("Phone Number:      " + professional.getPhoneNumber());

                            if(professional.isIdVerified()){
                                buttonID = (Button) findViewById(R.id.buttonID);
                                buttonSelfie = (Button) findViewById(R.id.buttonSelfie);
                                buttonID.setVisibility(View.INVISIBLE);
                                buttonSelfie.setVisibility(View.INVISIBLE);
                            }
                            if(professional.isGardaVetVer()){
                                buttonGardaVet = (Button) findViewById(R.id.buttonGardaVet);
                                buttonGardaVet.setVisibility(View.INVISIBLE);

                            }
                            if(professional.isSafePassVer()) {
                                buttonSafePass = (Button) findViewById(R.id.buttonSafePass);
                                buttonSafePass.setVisibility(View.INVISIBLE);
                            }
                            if((professional.isIdVerified())&&(professional.isGardaVetVer())&&(professional.isSafePassVer())) {
                                t9.setVisibility(View.INVISIBLE);
                            }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

    public void selectId(View v) {
        Intent intent = new Intent(this, ProfessionalUploadId.class);
        intent.putExtra("name", t6.getText().toString());
        startActivity(intent);
    }

    public void selectSelfie(View v) {
        Intent intent = new Intent(this, ProfessionalUploadSelfie.class);
        intent.putExtra("name", t6.getText().toString());
        startActivity(intent);
    }

    public void selectSafePass(View v) {
        Intent intent = new Intent(this, ProfessionalUploadSafePass.class);
        intent.putExtra("name", t6.getText().toString());
        startActivity(intent);
    }
    public void selectGardaVet(View v) {
        Intent intent = new Intent(this, ProfessionalUploadGardaVet.class);
        intent.putExtra("name", t6.getText().toString());
        startActivity(intent);
    }
}