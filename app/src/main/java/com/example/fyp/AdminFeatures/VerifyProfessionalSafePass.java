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

import com.example.fyp.ObjectClasses.Professional;
import com.example.fyp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class VerifyProfessionalSafePass extends AppCompatActivity {
    private TextView t1;
    private ImageView imageView1;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseStorage storage;
    private StorageReference storageReference1;
    String professionalId;
    private Professional professional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_professional_safe_pass);
        Intent intent = getIntent();
        professionalId = intent.getStringExtra("id");


        t1 = (TextView) findViewById(R.id.txtViewTitle);

        imageView1 = (ImageView) findViewById(R.id.imageView1);

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

                        t1.setText("Professional Name : " + professional.getName());
                        storageReference1 = storage.getReferenceFromUrl("gs://fypdatabase-d9dfe.appspot.com" + professional.getSafePassImage());

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
                                    Toast.makeText(VerifyProfessionalSafePass.this, "Image Failed to Load", Toast.LENGTH_SHORT).show();
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

    public void verifySafePass(View view) {
        ref.child("Professional").child(professionalId).child("safePassVer").setValue(true);
        Intent intent = new Intent(this, SelectedProfessionalToVerify.class);
        intent.putExtra("id", professionalId);
        startActivity(intent);
    }
}