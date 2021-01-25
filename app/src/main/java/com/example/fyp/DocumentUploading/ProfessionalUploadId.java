package com.example.fyp.DocumentUploading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fyp.CustomerFeatures.CustomerProfile;
import com.example.fyp.ProfessionalFeatures.ProfessionalProfile;
import com.example.fyp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfessionalUploadId extends AppCompatActivity {
    private DatabaseReference dbRef;
    private ImageView ivImage;
    private static final int PICK_IMAGE_REQUEST =22 ;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseUser user;
    private String uid;
    private Uri filePath;
    private String picPath,name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_upload_id);

        Intent intent = getIntent();
        name=intent.getStringExtra("name");

        dbRef= FirebaseDatabase.getInstance().getReference();
        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        ivImage = (ImageView) findViewById(R.id.imageView);
        user= FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
    }

    public void selectImage(View v) {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null) {

            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void uploadImage(View v) {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("professionalIdentityFiles/"+name+"IdDocument");
            picPath = ref.getPath();
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    dbRef.child("Professional").child(uid).child("idImage").setValue(picPath);

                                    Toast.makeText(ProfessionalUploadId.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ProfessionalUploadId.this, ProfessionalProfile.class);
                                    startActivity(intent);
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(ProfessionalUploadId.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
        }
    }


}