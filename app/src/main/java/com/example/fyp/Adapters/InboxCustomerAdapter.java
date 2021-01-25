package com.example.fyp.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.Messaging.CustomerSelectedConversation;
import com.example.fyp.Messaging.ProfessionalSelectedConversation;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.ObjectClasses.Message;
import com.example.fyp.R;

import java.util.ArrayList;

public class InboxCustomerAdapter extends RecyclerView.Adapter<InboxCustomerAdapter.MyViewHolder> {
    ArrayList<Message> messagesFromDb;

    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtView;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtView = (TextView) itemView.findViewById(R.id.textView);

        }

        @Override
        public void onClick(View view) {

        }
    }

    public InboxCustomerAdapter(ArrayList<Message> myDataset) {
        messagesFromDb = myDataset;
    }

    @Override
    public InboxCustomerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.row_layout, parent, false);
        InboxCustomerAdapter.MyViewHolder viewHolder = new InboxCustomerAdapter.MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InboxCustomerAdapter.MyViewHolder holder, int position) {

        final Message listing = messagesFromDb.get(position);
        holder.txtView.setText("Conversation Relating to Listing   : " + listing.getListingId() + "\n Professional's Id : " + listing.getProfessionalId());
        holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   int position=this.getLayoutPosition();
                Message selectedConvo = messagesFromDb.get(position);

                Intent intent = new Intent(v.getContext(), CustomerSelectedConversation.class);
                intent.putExtra("listingId", selectedConvo.getListingId());
              //  intent.putExtra("customerId", selectedConvo.getCustomerId());
                intent.putExtra("professionalId", selectedConvo.getProfessionalId());
                //    intent.putExtra("title",selectedConvo.getTitle());
                v.getContext().startActivity(intent);
            }
        });
    }

    public void add(int position, Message consultation) {
        messagesFromDb.add(position, consultation);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        messagesFromDb.remove(position);
        notifyItemRemoved(position);
    }

    public void update(Message consultation, int position) {
        messagesFromDb.set(position, consultation);
        notifyItemChanged(position);
    }

    public void addItemtoEnd(Message consultation) {
        //these functions are user-defined
        messagesFromDb.add(consultation);
        notifyItemInserted(messagesFromDb.size());
    }


    @Override
    public int getItemCount() {
        return messagesFromDb.size();
    }

}

