package com.example.fyp.ObjectClasses;

public class Message {

   // String professionalId;
   // String customerId;
    String conversationId;
    String content;
  //  String listingId;
    String dateTime;
    String from;
//
//    public Message(String professionalId, String customerId, String content, String listingId, String dateTime, String from) {
//        this.professionalId = professionalId;
//        this.customerId = customerId;
//        this.content = content;
//        this.listingId = listingId;
//        this.dateTime = dateTime;
//        this.from = from;
//    }


    public Message(String conversationId, String content, String dateTime, String from) {
        this.conversationId = conversationId;
        this.content = content;
        this.dateTime = dateTime;
        this.from = from;
    }

    public Message(){

    }

//   // public String getListingId() {
//        return listingId;
//    }
//
//    public void setListingId(String listingId) {
//        this.listingId = listingId;
//    }
//
//    public String getProfessionalId() {
//        return professionalId;
//    }
//
//    public void setProfessionalId(String professionalId) {
//        this.professionalId = professionalId;
//    }
//
//    public String getCustomerId() {
//        return customerId;
//    }
//
//    public void setCustomerId(String customerId) {
//        this.customerId = customerId;
//    }

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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
