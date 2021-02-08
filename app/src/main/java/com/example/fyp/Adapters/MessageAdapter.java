package com.example.fyp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.ObjectClasses.Consultation;
import com.example.fyp.ObjectClasses.Message;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    ArrayList<Message> messagesFromDB;

    FirebaseUser fuser;

    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtView;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtView = (TextView) itemView.findViewById(R.id.showMessage);

        }

        @Override
        public void onClick(View view) {

        }
    }

    public MessageAdapter(ArrayList<Message> myDataset) {
        messagesFromDB = myDataset;
    }

    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create new view - create a row - inflate the layout for the row
        if (viewType == MSG_TYPE_RIGHT) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.chat_item_right, parent, false);
            MessageAdapter.MyViewHolder viewHolder = new MessageAdapter.MyViewHolder(itemView);
            return viewHolder;
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.chat_item_left, parent, false);
            MessageAdapter.MyViewHolder viewHolder = new MessageAdapter.MyViewHolder(itemView);
            return viewHolder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {


        Message message = messagesFromDB.get(position);
        holder.txtView.setText(message.getContent() +"\n " +"\nFrom : "+message.getFrom()+"\n " + message.getDateTime());
    }

    public void add(int position, Message consultation) {
        messagesFromDB.add(position, consultation);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        messagesFromDB.remove(position);
        notifyItemRemoved(position);
    }

    public void update(com.example.fyp.ObjectClasses.Message consultation, int position) {
        messagesFromDB.set(position, consultation);
        notifyItemChanged(position);
    }

    public void addItemtoEnd(com.example.fyp.ObjectClasses.Message consultation) {
        //these functions are user-defined
        messagesFromDB.add(consultation);
        notifyItemInserted(messagesFromDB.size());
    }


    @Override
    public int getItemCount() {
        return messagesFromDB.size();
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
       // if (messagesFromDB.get(position).get().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        //} else
          //      return MSG_TYPE_LEFT;


        }
      //  return 0;


}



