package com.example.fyp.ObjectClasses;

public class Job {
    String jobId;
    Customer customer;
    Professional professional;
    Listing listing;
    int price;
    String location;
    String duration;
    String description;
    int quote;
    String feedbackFromCustomer;
    String feedbackFromProfessional;

    public Job(String jobId, Customer customer, Professional professional, Listing listing, int price, String location, String duration, String description, int quote, String feedbackFromCustomer, String feedbackFromProfessional) {
        this.jobId = jobId;
        this.customer = customer;
        this.professional = professional;
        this.listing = listing;
        this.price = price;
        this.location = location;
        this.duration = duration;
        this.description = description;
        this.quote = quote;
        this.feedbackFromCustomer = feedbackFromCustomer;
        this.feedbackFromProfessional = feedbackFromProfessional;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Professional getProfessional() {
        return professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuote() {
        return quote;
    }

    public void setQuote(int quote) {
        this.quote = quote;
    }

    public String getFeedbackFromCustomer() {
        return feedbackFromCustomer;
    }

    public void setFeedbackFromCustomer(String feedbackFromCustomer) {
        this.feedbackFromCustomer = feedbackFromCustomer;
    }

    public String getFeedbackFromProfessional() {
        return feedbackFromProfessional;
    }

    public void setFeedbackFromProfessional(String feedbackFromProfessional) {
        this.feedbackFromProfessional = feedbackFromProfessional;
    }
}
