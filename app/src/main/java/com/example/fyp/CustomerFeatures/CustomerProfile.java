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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.ObjectClasses.Customer;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.UUID;

public class CustomerProfile extends AppCompatActivity {
    private TextView t1,t2,t3,t4,t5,t6,t7,t8,t9;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private String uid;
    private Customer customer;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;
    private String picPath;
    private boolean idVer;
    private ImageView imgView;
    private Button selectImg,uploadImg;
    private static final int PICK_IMAGE_REQUEST =22 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        t1=(TextView)findViewById(R.id.textViewAddress);
        t2=(TextView)findViewById(R.id.textViewDob);
        t3=(TextView)findViewById(R.id.textViewEmail);
        t4=(TextView)findViewById(R.id.textViewIdVer);
        t5=(TextView)findViewById(R.id.textViewLocation);
        t6=(TextView)findViewById(R.id.textViewName);
        t7=(TextView)findViewById(R.id.textViewPassword);
        t8=(TextView)findViewById(R.id.textViewNumber);
        t9=(TextView)findViewById(R.id.textViewIdRequired);

        imgView = (ImageView) findViewById(R.id.imageViewId);

        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();
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
                        t1.setText("Address:     "+ customer.getAddress());
                        t2.setText("Date of Birth:      " + customer.getDob());
                        t3.setText("Email:      "+customer.getEmail());
                        t4.setText("Id Verified:      "+ customer.isIdVerified());
                        t5.setText("Location:     "+customer.getLocation());
                        t6.setText("Name:     "+customer.getName());
                        t7.setText("Password: ");
                        t8.setText("Phone Number:      "+customer.getPhoneNumber());
                        if (customer.isIdVerified()==true){
                            selectImg = (Button)findViewById(R.id.buttonSelectImage);
                            uploadImg = (Button)findViewById(R.id.buttonUploadId);

                            selectImg.setVisibility(View.INVISIBLE);
                            uploadImg.setVisibility(View.INVISIBLE);
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

    public void selectImage(View v){
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
                imgView.setImageBitmap(bitmap);
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

            StorageReference storeageRef = storageReference.child("images/"+ UUID.randomUUID().toString());
            picPath = storeageRef.getPath();
            storeageRef.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();

                                    Toast.makeText(CustomerProfile.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                    ref.child("Customer").child(uid).child("idVerified").setValue(true);

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(CustomerProfile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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