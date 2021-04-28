package com.example.fyp.Adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.ProfessionalFeatures.ProfSelectedListing;
import com.example.fyp.ProfessionalFeatures.SelectedCurrentJob;
import com.example.fyp.ProfessionalFeatures.SelectedNewJob;
import com.example.fyp.R;
import com.example.fyp.CustomerFeatures.SelectedListing;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MyAdapterProfessional extends RecyclerView.Adapter<MyAdapterProfessional.MyViewHolder> {

    ArrayList<Listing> listingsFromDB;

    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView t1,t2,t3,t4;
        public ImageView img;

        public MyViewHolder(View itemView){
            super(itemView);
            t1=(TextView)itemView.findViewById(R.id.txtViewTitle);
            t2=(TextView)itemView.findViewById(R.id.location);
            t3=(TextView)itemView.findViewById(R.id.tradeSector);
            t4=(TextView)itemView.findViewById(R.id.sched3);

            img=(ImageView)itemView.findViewById(R.id.imageView3);

        }

        @Override
        public void onClick(View view) {

            int position=this.getLayoutPosition();
            Listing selectedListing =listingsFromDB.get(position);
            Intent intent= new Intent(view.getContext(), SelectedListing.class);
            view.getContext().startActivity(intent);
        }
    }

    public MyAdapterProfessional(ArrayList<Listing>myDataset){
        listingsFromDB=myDataset;
    }
    @Override
    public MyAdapterProfessional.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.listing_recycler2,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final Listing name=listingsFromDB.get(position);

       FirebaseStorage storage = FirebaseStorage.getInstance();
       StorageReference storageReference = storage.getReferenceFromUrl("gs://fypdatabase-d9dfe.appspot.com" + name.getPhotos().get(0));
        try {
            final File file = File.createTempFile("image", "jpeg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    holder.img.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                     holder.img.setImageResource(R.drawable.listing);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.t1.setText(name.getTitle());
        holder.t2.setText(name.getLocation());
        holder.t3.setText(name.getTradeSector());

        holder.t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position=this.getLayoutPosition();
                Listing selectedListing =listingsFromDB.get(position);
                Intent intent= new Intent(v.getContext(), ProfSelectedListing.class);
                intent.putExtra("id",name.getListingId());
                intent.putExtra("title",name.getTitle());
                intent.putExtra("location",name.getLocation());
                v.getContext().startActivity(intent);
            }
        });
    }
    public void add(int position, Listing listing){
        listingsFromDB.add(position, listing);
        notifyItemInserted(position);
    }
    public void remove(int position){
        listingsFromDB.remove(position);
        notifyItemRemoved(position);
    }
    public void update(Listing listing,int position){
        listingsFromDB.set(position,listing);
        notifyItemChanged(position);
    }
    public void addItemtoEnd(Listing listing){
        //these functions are user-defined
        listingsFromDB.add(listing);
        notifyItemInserted(listingsFromDB.size());
    }


    @Override
    public int getItemCount() {
        return listingsFromDB.size();
    }

}
