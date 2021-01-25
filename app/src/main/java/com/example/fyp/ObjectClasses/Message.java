package com.example.fyp.ObjectClasses;

public class Message {

    String professionalId;
    String customerId;
    String content;
    String listingId;
    String dateTime;

    public Message(String professionalId, String customerId, String content, String listingId, String dateTime) {
        this.professionalId = professionalId;
        this.customerId = customerId;
        this.content = content;
        this.listingId = listingId;
        this.dateTime = dateTime;
    }

    public Message(){

    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(String professionalId) {
        this.professionalId = professionalId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
