package com.example.fyp.Adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.AdminFeatures.SelectedCustomerToVerify;
import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ProfessionalFeatures.SelectedCurrentJob;
import com.example.fyp.R;

import java.util.ArrayList;

public class CustomerVerificationAdapter extends RecyclerView.Adapter<CustomerVerificationAdapter.MyViewHolder>{

    ArrayList<Customer> customersFromDb;

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

    public CustomerVerificationAdapter(ArrayList<Customer>myDataset){
        customersFromDb =myDataset;
    }
    @Override
    public CustomerVerificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.row_layout,parent,false);
        CustomerVerificationAdapter.MyViewHolder viewHolder=new CustomerVerificationAdapter.MyViewHolder(itemView);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomerVerificationAdapter.MyViewHolder holder, int position) {

        final Customer name= customersFromDb.get(position);
        holder.txtView.setText("\nCustomer ID : "+name.getUsername()+"\n Name : "+name.getName()+"\n Email : "+ name.getEmail());
        holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position=this.getLayoutPosition();
                Customer selectedjob = customersFromDb.get(position);
                Intent intent= new Intent(v.getContext(), SelectedCustomerToVerify.class);
                intent.putExtra("id",name.getUsername());
                v.getContext().startActivity(intent);
            }
        });
    }
    public void add(int position, Customer listing){
        customersFromDb.add(position, listing);
        notifyItemInserted(position);
    }
    public void remove(int position){
        customersFromDb.remove(position);
        notifyItemRemoved(position);
    }
    public void update(Customer listing,int position){
        customersFromDb.set(position,listing);
        notifyItemChanged(position);
    }
    public void addItemtoEnd(Customer listing){
        //these functions are user-defined
        customersFromDb.add(listing);
        notifyItemInserted(customersFromDb.size());
    }


    @Override
    public int getItemCount() {
        return customersFromDb.size();
    }

}


