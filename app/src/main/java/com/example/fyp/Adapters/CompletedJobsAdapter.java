package com.example.fyp.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ProfessionalFeatures.CompletedSelectedJobs;
import com.example.fyp.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CompletedJobsAdapter extends RecyclerView.Adapter<CompletedJobsAdapter.MyViewHolder>{

    ArrayList<Job> jobsFromDb;

    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtView;
        public ImageView img;
        private FirebaseStorage storage;
        private StorageReference storageReference;

        public MyViewHolder(View itemView){
            super(itemView);
            txtView=(TextView)itemView.findViewById(R.id.textView);
            img=(ImageView)itemView.findViewById(R.id.imageView3);

        }

        @Override
        public void onClick(View view) {

//            int position=this.getLayoutPosition();
//            Listing selectedListing =listingsFromDB.get(position);
//            Intent intent= new Intent(view.getContext(),SelectedListing.class);
//            view.getContext().startActivity(intent);
        }
    }

    public CompletedJobsAdapter(ArrayList<Job>myDataset){
        jobsFromDb =myDataset;
    }
    @Override
    public CompletedJobsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.inbox_card_view,parent,false);
        CompletedJobsAdapter.MyViewHolder viewHolder=new CompletedJobsAdapter.MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedJobsAdapter.MyViewHolder holder, int position) {

        final Job name= jobsFromDb.get(position);

        holder.img.setImageResource(R.drawable.consultation);
        holder.txtView.setText("\nTitle : "+name.getJobTitle()+"\n Location : "+name.getLocation()
                +"\n Start Date :"+name.getStartDate()+"\n Estimated Duration "+name.getEstimatedDuration());
        holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position=this.getLayoutPosition();
                Job selectedjob = jobsFromDb.get(position);
                Intent intent= new Intent(v.getContext(), CompletedSelectedJobs.class);
                intent.putExtra("id",name.getJobId());
                v.getContext().startActivity(intent);
            }
        });
    }
    public void add(int position, Job listing){
        jobsFromDb.add(position, listing);
        notifyItemInserted(position);
    }
    public void remove(int position){
        jobsFromDb.remove(position);
        notifyItemRemoved(position);
    }
    public void update(Job listing,int position){
        jobsFromDb.set(position,listing);
        notifyItemChanged(position);
    }
    public void addItemtoEnd(Job listing){
        //these functions are user-defined
        jobsFromDb.add(listing);
        notifyItemInserted(jobsFromDb.size());
    }


    @Override
    public int getItemCount() {
        return jobsFromDb.size();
    }

}
