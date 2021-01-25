package com.example.fyp.CustomerFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fyp.ObjectClasses.Listing;
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
import java.util.ArrayList;
import java.util.UUID;

public class CreateListing extends AppCompatActivity {
    private DatabaseReference dbRef;
    private ImageView ivImage;
    private static final String Listing = "Listing";
    private static final int PICK_IMAGE_REQUEST =22 ;
    private EditText eircode, description,title;
    private Spinner trades;
    private boolean active = true;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseUser user;
    private String uid;
    private Uri filePath;
    private String picPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);

        dbRef=FirebaseDatabase.getInstance().getReference(Listing);
        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        ivImage = (ImageView) findViewById(R.id.imageView);
        user=FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();

    }
    public void postListing(View v) {
        description = (EditText) findViewById(R.id.editTextDescription);
        eircode = (EditText) findViewById(R.id.editTextEircode);
        title=(EditText)findViewById(R.id.editTextTitle) ;
        trades = (Spinner) findViewById(R.id.spinnerTrades);
        uploadImage();
        String keyId = dbRef.push().getKey();

       // String strUrl = url.getText().toString();

        String strDescription = description.getText().toString();
        String strEircode = eircode.getText().toString();
        String strTrade = trades.getSelectedItem().toString();
        String listingId=keyId;
        String listingTitle=title.getText().toString();
        ArrayList<String> photos = new ArrayList<String>();
        photos.add(picPath);

        Listing listing = new Listing(active, photos, strDescription, strEircode, strTrade,uid,listingId,listingTitle,"s");
      //  String keyId = dbRef.push().getKey();
        dbRef.child(keyId).setValue(listing);

        Toast.makeText(CreateListing.this, "Listing successfully created ", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(CreateListing.this, WelcomeCustomer.class);
        startActivity(intent);


    }

    //
    //
    //METHODS FOR ADDING IMAGE
    //
    //
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

    public void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            picPath = ref.getPath();
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    // dbRef.child("User").child(uid).child("profileImageUri").setValue(filePath);

                                    Toast.makeText(CreateListing.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(CreateListing.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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