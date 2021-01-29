package com.example.fyp.AdminFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.ObjectClasses.Professional;
import com.example.fyp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class SelectedProfessionalToVerify extends AppCompatActivity {
    private TextView t1, t2, t3, t4,t5;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    String professionalId;
    private Professional professional;
    Button idButton,safePassButton,gardaVetButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_professional_to_verify);
        Intent intent = getIntent();
        professionalId = intent.getStringExtra("id");

        t1 = (TextView) findViewById(R.id.txtViewTitle);
        t2 = (TextView) findViewById(R.id.txtViewAddress);
        t3 = (TextView) findViewById(R.id.txtViewTrade);
        t4 = (TextView) findViewById(R.id.textViewEmail);
        t5 = (TextView) findViewById(R.id.textViewDob);

        idButton=(Button)findViewById(R.id.verifyIdButton);
        safePassButton=(Button)findViewById(R.id.verifySafePassButton);
        gardaVetButton=(Button)findViewById(R.id.verifyGardaVetButton);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();


        getProfessional();
    }

    public void getProfessional() {
        ref.child("Professional").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(professionalId)) {
                        professional = child.getValue(Professional.class);
                        t1.setText("Currently Selected : "+professional.getName());
                        t2.setText("Address : "+professional.getAddress() );
                        t3.setText("Trade : "+professional.getTrade() );
                        t4.setText("Email : "+professional.getEmail());
                        t5.setText("Date of Birth : "+professional.getDob());

                        if(professional.isIdVerified()){
                            idButton.setVisibility(View.INVISIBLE);
                        }
                        if(professional.isSafePassVer()){
                            safePassButton.setVisibility(View.INVISIBLE);
                        }
                        if(professional.isGardaVetVer()){
                            gardaVetButton.setVisibility(View.INVISIBLE);
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

    public void verifyId(View view) {

        Intent intent=new Intent(this,VerifyProfessionalID.class);
        intent.putExtra("id",professionalId);
        startActivity(intent);

    }

    public void verifyGardaVet(View view) {
        Intent intent=new Intent(this,VerifyProfessionalGardaVet.class);
        intent.putExtra("id",professionalId);
        startActivity(intent);
    }

    public void verifySafePass(View view) {
        Intent intent=new Intent(this,VerifyProfessionalSafePass.class);
        intent.putExtra("id",professionalId);
        startActivity(intent);
    }

    public void home(View view) {
        Intent intent=new Intent(this,WelcomeAdmin.class);
        startActivity(intent);
    }
}