package com.example.fyp.ProfessionalFeatures;

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

import com.example.fyp.ObjectClasses.Listing;
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

public class SelectedJob extends AppCompatActivity {

    private TextView t1, t2, t3,t4;
    private ImageView imgView;

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseUser user;
    private String uid;
    private String listingId;
    private Listing listing;
    private String photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_job);

        t1 = (TextView) findViewById(R.id.txtViewEircode);
        t2 = (TextView) findViewById(R.id.txtViewTrade);
        t3 = (TextView) findViewById(R.id.textViewDescription);
        t4 = (TextView) findViewById(R.id.txtViewTitle);

        imgView = (ImageView) findViewById(R.id.imageView);

        storage = FirebaseStorage.getInstance();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        Intent i = getIntent();
        listingId = i.getStringExtra("id");
        t4.setText("Listing id "+ listingId);
        //   t1.setText(id);
        getListing();
        // displayListing();

    }

    public void getListing() {
        ref.child("Listing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(listingId)) {
                        listing = child.getValue(Listing.class);
                        photoUrl = listing.getPhotos().get(0);
                        displayListing();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

    public void displayListing() {
        t1.setText("Listing location : " + listing.getLocation());
        t2.setText("Trade Sector : " + listing.getTradeSector());
        t3.setText("Description : " + listing.getDescription());

        storageReference = storage.getReferenceFromUrl("gs://fypdatabase-d9dfe.appspot.com"+photoUrl);

//fypdatabase-d9dfe.appspot.com/images
        try {
            final File file = File.createTempFile("image", "jpeg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    imgView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SelectedJob.this, "Image Failed to Load", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        // imgView.setImageBitmap();
    }

    public void backToBrowse(View v){
        Intent intent = new Intent(SelectedJob.this, BrowseJobs.class);
        startActivity(intent);
    }

    public void arrangeQuote(View v){
        Intent intent = new Intent(SelectedJob.this, ArrangeConsultation.class);
        intent.putExtra("listingId",listingId);
        startActivity(intent);
    }

}