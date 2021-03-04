package com.example.fyp.ObjectClasses;

public class Consultation {

    String date;
    String time;
    String location;
    String customerId;
    String professionalId;
    String listingId;
    boolean accepted;
    boolean finished;
    String message;
    String consultationId;
    boolean denied;
    String title;

    public Consultation() {

    }

    public Consultation(String date, String time, String location, String customerId, String professionalId,
                        String listingId, boolean accepted, boolean finished, String message, String consultationId, boolean denied, String title) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.customerId = customerId;
        this.professionalId = professionalId;
        this.listingId = listingId;
        this.accepted = accepted;
        this.finished = finished;
        this.message = message;
        this.consultationId = consultationId;
        this.denied = denied;
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(String professionalId) {
        this.professionalId = professionalId;
    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(String consultationId) {
        this.consultationId = consultationId;
    }

    public boolean isDenied() {
        return denied;
    }

    public void setDenied(boolean denied) {
        this.denied = denied;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
