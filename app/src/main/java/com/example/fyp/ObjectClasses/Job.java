package com.example.fyp.ObjectClasses;

public class Job {
    String jobId;
    String customerId;
    String professionalId;
    Consultation consultation;
    Listing listing;
    int price;
    String location;
    String estimatedDuration;
    String description;
    int quote;
    String feedbackFromCustomer;
    String feedbackFromProfessional;
    boolean finished;
    String startDate;
    String endDate;
    String actualDuration;
    String priceBreakdown;
    String jobTitle;
  //  String professionalStripeKey;

    public Job(){

    }

    public Job(String jobId, String customerId, String professionalId, Consultation consultation, Listing listing, int price, String location, String estimatedDuration, String description, int quote, String feedbackFromCustomer, String feedbackFromProfessional,
               boolean finished, String startDate, String endDate, String actualDuration, String priceBreakdown, String jobTitle) {
        this.jobId = jobId;
        this.customerId = customerId;
        this.professionalId = professionalId;
        this.consultation = consultation;
        this.listing = listing;
        this.price = price;
        this.location = location;
        this.estimatedDuration = estimatedDuration;
        this.description = description;
        this.quote = quote;
        this.feedbackFromCustomer = feedbackFromCustomer;
        this.feedbackFromProfessional = feedbackFromProfessional;
        this.finished = finished;
        this.startDate = startDate;
        this.endDate = endDate;
        this.actualDuration = actualDuration;
        this.priceBreakdown = priceBreakdown;
        this.jobTitle = jobTitle;
//        this.professionalStripeKey = professionalStripeKey;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuote() {
        return quote;
    }

//    public String getProfessionalStripeKey() {
//        return professionalStripeKey;
//    }
//
//    public void setProfessionalStripeKey(String professionalStripeKey) {
//        this.professionalStripeKey = professionalStripeKey;
//    }

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

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(String estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public String getActualDuration() {
        return actualDuration;
    }

    public void setActualDuration(String actualDuration) {
        this.actualDuration = actualDuration;
    }

    public String getPriceBreakdown() {
        return priceBreakdown;
    }

    public void setPriceBreakdown(String priceBreakdown) {
        this.priceBreakdown = priceBreakdown;
    }
}
