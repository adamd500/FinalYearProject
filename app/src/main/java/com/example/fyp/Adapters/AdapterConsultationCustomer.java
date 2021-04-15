package com.example.fyp.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.CustomerFeatures.SelectedConsultation;
import com.example.fyp.ObjectClasses.Consultation;
import com.example.fyp.R;

import java.util.ArrayList;

public class AdapterConsultationCustomer  extends RecyclerView.Adapter<AdapterConsultationCustomer.MyViewHolder>{

    ArrayList<Consultation> consultationsFromDB;

    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView t1,t2,t3,t4;

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

    public AdapterConsultationCustomer(ArrayList<Consultation>myDataset){
        consultationsFromDB=myDataset;
    }
    @Override
    public AdapterConsultationCustomer.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.customer_consultation_recycler,parent,false);
        AdapterConsultationCustomer.MyViewHolder viewHolder=new AdapterConsultationCustomer.MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterConsultationCustomer.MyViewHolder holder, int position) {

        final Consultation consultation=consultationsFromDB.get(position);
        holder.t1.setText(consultation.getTitle());
        holder.t2.setText(consultation.getDate());
        holder.t3.setText(consultation.getTime());

        holder.t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position=this.getLayoutPosition();
              //  notifyDataSetChanged();
                Consultation selectedConsultation =consultationsFromDB.get(position);
                Intent intent= new Intent(v.getContext(), SelectedConsultation.class);
                intent.putExtra("id",selectedConsultation.getConsultationId());
                v.getContext().startActivity(intent);
               // notifyDataSetChanged();
            }
        });
    }
    public void add(int position, Consultation consultation){
        consultationsFromDB.add(position, consultation);
        notifyItemInserted(position);
    }
    public void remove(int position){
        consultationsFromDB.remove(position);
        notifyItemRemoved(position);
    }
    public void update(Consultation consultation,int position){
        consultationsFromDB.set(position,consultation);
        notifyItemChanged(position);
    }
    public void addItemtoEnd(Consultation consultation){
        //these functions are user-defined
        consultationsFromDB.add(consultation);
        notifyItemInserted(consultationsFromDB.size());
    }


    @Override
    public int getItemCount() {
        return consultationsFromDB.size();
    }

}


