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
import com.example.fyp.ProfessionalFeatures.SelectedAcceptedConsultation;
import com.example.fyp.R;

import java.util.ArrayList;

public class AcceptedConsultationAdapter extends RecyclerView.Adapter<AcceptedConsultationAdapter.MyViewHolder> {
    ArrayList<Consultation> consultationsFromDB;

    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView t1,t2,t3,t4;

        public MyViewHolder(View itemView){
            super(itemView);
            t1=(TextView)itemView.findViewById(R.id.listingTitle);
            t2=(TextView)itemView.findViewById(R.id.proposedDate);
            t3=(TextView)itemView.findViewById(R.id.proposedTime);
            t4=(TextView)itemView.findViewById(R.id.sched3);

        }

        @Override
        public void onClick(View view) {

        }
    }

    public AcceptedConsultationAdapter(ArrayList<Consultation>myDataset){
        consultationsFromDB=myDataset;
    }
    @Override
    public AcceptedConsultationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.accepted_cons,parent,false);
        AcceptedConsultationAdapter.MyViewHolder viewHolder=new AcceptedConsultationAdapter.MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedConsultationAdapter.MyViewHolder holder, int position) {

        final Consultation consultation=consultationsFromDB.get(position);
        holder.t1.setText(consultation.getTitle());
        holder.t2.setText(consultation.getDate());
        holder.t3.setText(consultation.getTime());

        holder.t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position=this.getLayoutPosition();
                notifyDataSetChanged();
                Consultation selectedConsultation =consultationsFromDB.get(position);
                Intent intent= new Intent(v.getContext(), SelectedAcceptedConsultation.class);
                intent.putExtra("id",consultation.getConsultationId());

                v.getContext().startActivity(intent);
                notifyDataSetChanged();

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
