package com.example.fyp.ObjectClasses;

import java.util.ArrayList;

public class Conversation {

    String conversationId;
    String customerId;
    String customerName;
    String listingId;
    String listingTitle;
    String professionalId;
    String professionalName;
    ArrayList<Message>messages=new ArrayList<Message>();

    public Conversation(String conversationId, String customerId, String customerName, String listingId, String listingTitle, String professionalId, String professionalName, ArrayList<Message> messages) {
        this.conversationId = conversationId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.listingId = listingId;
        this.listingTitle = listingTitle;
        this.professionalId = professionalId;
        this.professionalName = professionalName;
        this.messages = messages;
    }

    public Conversation(){

    }
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getListingTitle() {
        return listingTitle;
    }

    public void setListingTitle(String listingTitle) {
        this.listingTitle = listingTitle;
    }

    public String getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(String professionalId) {
        this.professionalId = professionalId;
    }

    public String getProfessionalName() {
        return professionalName;
    }

    public void setProfessionalName(String professionalName) {
        this.professionalName = professionalName;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
