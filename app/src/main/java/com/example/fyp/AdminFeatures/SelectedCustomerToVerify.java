package com.example.fyp.AdminFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.ProfessionalFeatures.SelectedNewJob;
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

public class SelectedCustomerToVerify extends AppCompatActivity {
    private TextView t1, t2, t3, t4;
    private ImageView imageView1, imageView2;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseStorage storage;
    private StorageReference storageReference1,storageReference2;
    private FirebaseUser user;
    private String uid;
    private String photoUrlDocument, photUrlSelfie;
    String customerId;
    private Customer customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_customer_to_verify);

        Intent intent = getIntent();
        customerId = intent.getStringExtra("id");

        t1 = (TextView) findViewById(R.id.txtViewEircode);
        t2 = (TextView) findViewById(R.id.txtViewTrade);
        t3 = (TextView) findViewById(R.id.textViewDescription);
        t4 = (TextView) findViewById(R.id.txtViewTitle);

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);

        storage = FirebaseStorage.getInstance();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        getCustomer();
    }
    public void getCustomer() {
        ref.child("Customer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(customerId)) {
                        customer = child.getValue(Customer.class);
                        t4.setText("Currently Selected : " + customer.getName());

                        if (customer.getSelfieImage() != null && customer.getIdImage() != null) {
                            storageReference1 = storage.getReferenceFromUrl("gs://fypdatabase-d9dfe.appspot.com" + customer.getIdImage());
                            storageReference2 = storage.getReferenceFromUrl("gs://fypdatabase-d9dfe.appspot.com" + customer.getSelfieImage());

//fypdatabase-d9dfe.appspot.com/images
                            try {
                                final File file1 = File.createTempFile("image", "jpeg");
                                storageReference1.getFile(file1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(file1.getAbsolutePath());
                                        imageView1.setImageBitmap(bitmap);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SelectedCustomerToVerify.this, "Image Failed to Load", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            try {
                                final File file2 = File.createTempFile("image", "jpeg");
                                storageReference2.getFile(file2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                                        imageView2.setImageBitmap(bitmap);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SelectedCustomerToVerify.this, "Image Failed to Load", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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

    public void verifyCustomer(View view) {
        VerifyCustomers.myAdapter.notifyDataSetChanged();
        ref.child("Customer").child(customerId).child("idVerified").setValue(true);

        Intent intent = new Intent(this, WelcomeAdmin.class);
        startActivity(intent);
    }
}