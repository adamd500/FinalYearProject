package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.ObjectClasses.Listing;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterCustomer extends RecyclerView.Adapter<MyAdapterCustomer.MyViewHolder> {

     ArrayList<Listing> listingsFromDB;

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

    public MyAdapterCustomer(ArrayList<Listing>myDataset){
        listingsFromDB=myDataset;
    }
    @Override
    public MyAdapterCustomer.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.row_layout,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Listing name=listingsFromDB.get(position);
        holder.txtView.setText("\n Location : "+name.getLocation()+"\n Trade Sector : "+name.getTradeSector()+"\n Description : "+ name.getDescription());
        holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position=this.getLayoutPosition();
                Listing selectedListing =listingsFromDB.get(position);
                Intent intent= new Intent(v.getContext(),SelectedListing.class);
                intent.putExtra("id",name.getListingId());
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
