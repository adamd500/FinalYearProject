package com.example.fyp.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ProfessionalFeatures.SelectedCurrentJob;
import com.example.fyp.R;

import java.util.ArrayList;

public class CurrentJobsAdapter extends RecyclerView.Adapter<CurrentJobsAdapter.MyViewHolder>{

    ArrayList<Job> jobsFromDb;

    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtView;

        public MyViewHolder(View itemView){
            super(itemView);
            txtView=(TextView)itemView.findViewById(R.id.textView);

        }

        @Override
        public void onClick(View view) {

//            int position=this.getLayoutPosition();
//            Listing selectedListing =listingsFromDB.get(position);
//            Intent intent= new Intent(view.getContext(),SelectedListing.class);
//            view.getContext().startActivity(intent);
        }
    }

    public CurrentJobsAdapter(ArrayList<Job>myDataset){
        jobsFromDb =myDataset;
    }
    @Override
    public CurrentJobsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.row_layout,parent,false);
        CurrentJobsAdapter.MyViewHolder viewHolder=new CurrentJobsAdapter.MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentJobsAdapter.MyViewHolder holder, int position) {

        final Job name= jobsFromDb.get(position);
        holder.txtView.setText("\nJob ID : "+name.getJobId()+"\n Location : "+name.getLocation()+"\n Description : "+ name.getDescription()
                +"\n Start Date :"+name.getStartDate()+"\n Estimated Duration "+name.getEstimatedDuration());
        holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position=this.getLayoutPosition();
                Job selectedjob = jobsFromDb.get(position);
                Intent intent= new Intent(v.getContext(), SelectedCurrentJob.class);
                intent.putExtra("id",name.getJobId());
                intent.putExtra("customerId",name.getCustomerId());

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

