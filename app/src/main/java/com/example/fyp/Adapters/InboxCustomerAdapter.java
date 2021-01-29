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
import com.example.fyp.ObjectClasses.Conversation;
import com.example.fyp.ObjectClasses.Message;
import com.example.fyp.R;

import java.util.ArrayList;

public class InboxCustomerAdapter extends RecyclerView.Adapter<InboxCustomerAdapter.MyViewHolder> {
    ArrayList<Conversation> conversationsFromDb;

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

    public InboxCustomerAdapter(ArrayList<Conversation> myDataset) {
        conversationsFromDb = myDataset;
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

        final Conversation listing= conversationsFromDb.get(position);
        holder.txtView.setText("Conversation in Relation to Listing Titled  : "+listing.getListingTitle()+"\n Professionals Name : "+listing.getProfessionalName());
        holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   int position=this.getLayoutPosition();
                Conversation selectedConvo = conversationsFromDb.get(position);

                Intent intent= new Intent(v.getContext(), CustomerSelectedConversation.class);

                intent.putExtra("conversationId",selectedConvo.getConversationId());
                intent.putExtra("professionalId",selectedConvo.getProfessionalId());
                intent.putExtra("listingId",selectedConvo.getListingId());
                //   intent.putExtra("title",selectedConvo.getTitle());
                v.getContext().startActivity(intent);
            }
        });
    }

    public void add(int position, Conversation consultation) {
        conversationsFromDb.add(position, consultation);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        conversationsFromDb.remove(position);
        notifyItemRemoved(position);
    }

    public void update(Conversation consultation, int position) {
        conversationsFromDb.set(position, consultation);
        notifyItemChanged(position);
    }

    public void addItemtoEnd(Conversation consultation) {
        //these functions are user-defined
        conversationsFromDb.add(consultation);
        notifyItemInserted(conversationsFromDb.size());
    }


    @Override
    public int getItemCount() {
        return conversationsFromDb.size();
    }

}

