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

public class VerifyProfessionalID extends AppCompatActivity {
    private TextView t1;
    private ImageView imageView1, imageView2;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseStorage storage;
    private StorageReference storageReference1,storageReference2;
    String professionalId;
    private Professional professional;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_professional_i_d);

        Intent intent = getIntent();
        professionalId = intent.getStringExtra("id");


        t1 = (TextView) findViewById(R.id.txtViewTitle);

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);

        storage = FirebaseStorage.getInstance();

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

                        t1.setText("Professional Name : "+professional.getName() );
                        storageReference1 = storage.getReferenceFromUrl("gs://fypdatabase-d9dfe.appspot.com" + professional.getIdImage());
                        storageReference2 = storage.getReferenceFromUrl("gs://fypdatabase-d9dfe.appspot.com" + professional.getSelfieImage());

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
                                    Toast.makeText(VerifyProfessionalID.this, "Image Failed to Load", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(VerifyProfessionalID.this, "Image Failed to Load", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
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
    public void verifyIdDocument(View view) {
        ref.child("Professional").child(professionalId).child("idVerified").setValue(true);
        Intent intent=new Intent(this,SelectedProfessionalToVerify.class);
        intent.putExtra("id",professionalId);
        startActivity(intent);
    }
}