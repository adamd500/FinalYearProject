package com.example.fyp.CustomerFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.DocumentUploading.CustomerUploadIdDocument;
import com.example.fyp.DocumentUploading.CustomerUploadSelfieImage;
import com.example.fyp.Messaging.InboxCustomer;
import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class CustomerProfile extends AppCompatActivity {
    private TextView t1, t2, t3, t4, t5, t6, t7, t8, t9;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private String uid;
    private Customer customer;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath1;
    private String picPath;
    private boolean idVer;
    private Button buttonID, buttonSelfie;
    private static final int PICK_IMAGE_REQUEST = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        t1 = (TextView) findViewById(R.id.textViewAddress);
        t2 = (TextView) findViewById(R.id.textViewDob);
        t3 = (TextView) findViewById(R.id.textViewEmail);
        t4 = (TextView) findViewById(R.id.textViewIdVer);
        t5 = (TextView) findViewById(R.id.textViewLocation);
        t6 = (TextView) findViewById(R.id.textViewName);
        t7 = (TextView) findViewById(R.id.textViewPassword);
        t8 = (TextView) findViewById(R.id.textViewNumber);
        t9 = (TextView) findViewById(R.id.textViewIdRequired);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        storage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        ref.child("Customer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(uid)) {
                        customer = child.getValue(Customer.class);
                        t1.setText("Address:     " + customer.getAddress());
                        t2.setText("Date of Birth:      " + customer.getDob());
                        t3.setText("Email:      " + customer.getEmail());
                        t4.setText("Id Verified:      " + customer.isIdVerified());
                        t5.setText("Location:     " + customer.getLocation());
                        t6.setText(customer.getName());
                        t7.setText("Password: ");
                        t8.setText("Phone Number:      " + customer.getPhoneNumber());
                        if (customer.isIdVerified()) {
                            buttonID = (Button) findViewById(R.id.buttonID);
                            buttonSelfie = (Button) findViewById(R.id.buttonSelfie);

                            buttonID.setVisibility(View.INVISIBLE);
                            buttonSelfie.setVisibility(View.INVISIBLE);
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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(CustomerProfile.this, InboxCustomer.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(CustomerProfile.this, CustomerProfile.class);
                        startActivity(intent1);
                        return true;

                    case R.id.createListing:
                        Intent intent2 = new Intent(CustomerProfile.this, CustomerListingNav.class);
                        startActivity(intent2);
                        return true;

                    case R.id.myListings:
                        Intent intent3 = new Intent(CustomerProfile.this, CustomerAllListings.class);
                        startActivity(intent3);
                        return true;


                    case R.id.home:
                        Intent intent4 = new Intent(CustomerProfile.this, WelcomeCustomer.class);
                        startActivity(intent4);
                }

                return false;
            }
        });
    }

    public void selectId(View v) {
        Intent intent = new Intent(this, CustomerUploadIdDocument.class);
        intent.putExtra("name", t6.getText().toString());
        startActivity(intent);
    }

    public void selectSelfie(View v) {
        Intent intent = new Intent(this, CustomerUploadSelfieImage.class);
        intent.putExtra("name", t6.getText().toString());
        startActivity(intent);
    }


}