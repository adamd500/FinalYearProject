package com.example.fyp.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.CustomerFeatures.SelectedListing;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.R;

import java.util.ArrayList;

public class ProfessionalFeedbackAdapter extends RecyclerView.Adapter<ProfessionalFeedbackAdapter.MyViewHolder> {

    ArrayList<String> feedbackFromDB;

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

    public ProfessionalFeedbackAdapter(ArrayList<String>myDataset){
        feedbackFromDB =myDataset;
    }
    @Override
    public ProfessionalFeedbackAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.row_layout,parent,false);
        ProfessionalFeedbackAdapter.MyViewHolder viewHolder=new ProfessionalFeedbackAdapter.MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfessionalFeedbackAdapter.MyViewHolder holder, int position) {

        final String name= feedbackFromDB.get(position);
        holder.txtView.setText("\n"+"   "+name+"\n");
        holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void add(int position, String listing){
        feedbackFromDB.add(position, listing);
        notifyItemInserted(position);
    }
    public void remove(int position){
        feedbackFromDB.remove(position);
        notifyItemRemoved(position);
    }
    public void update(String listing,int position){
        feedbackFromDB.set(position,listing);
        notifyItemChanged(position);
    }
    public void addItemtoEnd(String listing){
        //these functions are user-defined
        feedbackFromDB.add(listing);
        notifyItemInserted(feedbackFromDB.size());
    }


    @Override
    public int getItemCount() {
        return feedbackFromDB.size();
    }

}
