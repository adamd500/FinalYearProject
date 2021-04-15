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
import com.example.fyp.AdminFeatures.SelectedProfessionalToVerify;
import com.example.fyp.ObjectClasses.Customer;
import com.example.fyp.ObjectClasses.Professional;
import com.example.fyp.R;

import java.util.ArrayList;

public class ProfessionalVerificationAdapter extends RecyclerView.Adapter<ProfessionalVerificationAdapter.MyViewHolder>{

    ArrayList<Professional> professionalsFromDb;

    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtView;

        public MyViewHolder(View itemView){
            super(itemView);
            txtView=(TextView)itemView.findViewById(R.id.textView);

        }

        @Override
        public void onClick(View view) {

        }
    }

    public ProfessionalVerificationAdapter(ArrayList<Professional>myDataset){
        professionalsFromDb =myDataset;
    }
    @Override
    public ProfessionalVerificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.row_layout,parent,false);
        ProfessionalVerificationAdapter.MyViewHolder viewHolder=new ProfessionalVerificationAdapter.MyViewHolder(itemView);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProfessionalVerificationAdapter.MyViewHolder holder, int position) {

        final Professional name= professionalsFromDb.get(position);
        holder.txtView.setText("\nProfessional ID : "+name.getUsername()+"\n Name : "+name.getName()+"\n Email : "+ name.getEmail());
        holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position=this.getLayoutPosition();
                Professional selectedjob = professionalsFromDb.get(position);
                Intent intent= new Intent(v.getContext(), SelectedProfessionalToVerify.class);
                intent.putExtra("id",name.getUsername());
                v.getContext().startActivity(intent);
            }
        });
    }
    public void add(int position, Professional listing){
        professionalsFromDb.add(position, listing);
        notifyItemInserted(position);
    }
    public void remove(int position){
        professionalsFromDb.remove(position);
        notifyItemRemoved(position);
    }
    public void update(Professional listing,int position){
        professionalsFromDb.set(position,listing);
        notifyItemChanged(position);
    }
    public void addItemtoEnd(Professional listing){
        //these functions are user-defined
        professionalsFromDb.add(listing);
        notifyItemInserted(professionalsFromDb.size());
    }


    @Override
    public int getItemCount() {
        return professionalsFromDb.size();
    }

}
