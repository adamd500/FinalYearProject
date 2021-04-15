package com.example.fyp.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.CustomerFeatures.FinaliseJobCustomer;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ProfessionalFeatures.SelectedCurrentJob;
import com.example.fyp.R;

import java.util.ArrayList;

public class JobsForFinalisingAdapter extends RecyclerView.Adapter<JobsForFinalisingAdapter.MyViewHolder>{
    ArrayList<Job> jobsFromDb;
    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       // public  TextView txtView;
       public  TextView t1,t2,t3,t4;

        public MyViewHolder(View itemView){
            super(itemView);
            t1=(TextView)itemView.findViewById(R.id.txtViewTitle);
            t2=(TextView)itemView.findViewById(R.id.location);
            t3=(TextView)itemView.findViewById(R.id.tradeSector);
            t4=(TextView)itemView.findViewById(R.id.sched3);

        }

        @Override
        public void onClick(View view) {


        }
    }

    public JobsForFinalisingAdapter(ArrayList<Job>myDataset){
        jobsFromDb =myDataset;
    }
    @Override
    public JobsForFinalisingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.finalise_job_recylcer,parent,false);
        JobsForFinalisingAdapter.MyViewHolder viewHolder=new JobsForFinalisingAdapter.MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull JobsForFinalisingAdapter.MyViewHolder holder, int position) {

        final Job name= jobsFromDb.get(position);

        holder.t1.setText(name.getJobTitle());
        holder.t2.setText(name.getStartDate());
        holder.t3.setText(name.getEndDate());

        holder.t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                //int position=this.getLayoutPosition();
                Job selectedjob = jobsFromDb.get(position);
                Intent intent= new Intent(v.getContext(), FinaliseJobCustomer.class);
                intent.putExtra("id",name.getJobId());
                intent.putExtra("price", String.valueOf(name.getPrice()));
                intent.putExtra("professionalId",name.getProfessionalId());
                v.getContext().startActivity(intent);
                notifyDataSetChanged();

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
