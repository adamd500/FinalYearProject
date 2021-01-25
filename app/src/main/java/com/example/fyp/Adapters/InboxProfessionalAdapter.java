package com.example.fyp.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.Messaging.ProfessionalSelectedConversation;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.R;

import java.util.ArrayList;

public class InboxProfessionalAdapter extends RecyclerView.Adapter<InboxProfessionalAdapter.MyViewHolder> {
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

        }
    }

    public InboxProfessionalAdapter(ArrayList<Listing>myDataset){
        listingsFromDB =myDataset;
    }
    @Override
    public InboxProfessionalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.row_layout,parent,false);
        InboxProfessionalAdapter.MyViewHolder viewHolder=new InboxProfessionalAdapter.MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InboxProfessionalAdapter.MyViewHolder holder, int position) {

        final Listing listing= listingsFromDB.get(position);
        holder.txtView.setText("Conversation in Relation to Listing Titled  : "+listing.getTitle()+"\n Listing Description : "+listing.getDescription()
                +"\n Listing location : "+listing.getLocation());
        holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   int position=this.getLayoutPosition();
                Listing selectedConvo = listingsFromDB.get(position);

                Intent intent= new Intent(v.getContext(), ProfessionalSelectedConversation.class);
                intent.putExtra("listingId",selectedConvo.getListingId());
                intent.putExtra("customerId",selectedConvo.getCustomerUsername());
                intent.putExtra("professionalId",selectedConvo.getProfessionalAssignedId());
                intent.putExtra("title",selectedConvo.getTitle());
                v.getContext().startActivity(intent);
            }
        });
    }
    public void add(int position, Listing consultation){
        listingsFromDB.add(position, consultation);
        notifyItemInserted(position);
    }
    public void remove(int position){
        listingsFromDB.remove(position);
        notifyItemRemoved(position);
    }
    public void update(Listing consultation,int position){
        listingsFromDB.set(position,consultation);
        notifyItemChanged(position);
    }
    public void addItemtoEnd(Listing consultation){
        //these functions are user-defined
        listingsFromDB.add(consultation);
        notifyItemInserted(listingsFromDB.size());
    }


    @Override
    public int getItemCount() {
        return listingsFromDB.size();
    }

}
