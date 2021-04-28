package com.example.fyp.ObjectClasses;

public class Message {

    String conversationId;
    String content;
    String dateTime;
    String from;



    public Message(String conversationId, String content, String dateTime, String from) {
        this.conversationId = conversationId;
        this.content = content;
        this.dateTime = dateTime;
        this.from = from;
    }

    public Message(){

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
