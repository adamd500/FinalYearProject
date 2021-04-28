package com.example.fyp.ObjectClasses;

import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ObjectClasses.Person;

import java.util.ArrayList;
import java.util.List;

public class Professional extends Person {

    boolean safePassVer;
    boolean gardaVetVer;
    String trade;
    int workRadius;
    String safePassImage;
    String gardaVetImage;
    int punctuality;
    int workQuality;
    int communication;
    int overallCustomerSatisfaction;
    int jobsCompleted;
    int totalEarned;
    String stripeKey;

    public Professional() {

    }

    public Professional(String name, String dob, String address, boolean idVerified, List<String> feedback, String location, String phoneNumber, String email,
                        String password, String username, String selfieImage, String idImage, boolean safePassVer, boolean gardaVetVer, String trade, int workRadius, String safePassImage, String gardaVetImage, int punctuality,
                        int workQuality, int communication, int overallCustomerSatisfaction, int jobsCompleted, int totalEarned, String stripeKey) {
        super(name, dob, address, idVerified, feedback, location, phoneNumber, email, password, username, selfieImage, idImage);
        this.safePassVer = safePassVer;
        this.gardaVetVer = gardaVetVer;
        this.trade = trade;
        this.workRadius = workRadius;
        this.safePassImage = safePassImage;
        this.gardaVetImage = gardaVetImage;
        this.punctuality = punctuality;
        this.workQuality = workQuality;
        this.communication = communication;
        this.overallCustomerSatisfaction = overallCustomerSatisfaction;
        this.jobsCompleted = jobsCompleted;
        this.totalEarned = totalEarned;
        this.stripeKey = stripeKey;
    }

    public String getStripeKey() {
        return stripeKey;
    }

    public void setStripeKey(String stripeKey) {
        this.stripeKey = stripeKey;
    }

    public boolean isSafePassVer() {
        return safePassVer;
    }

    public void setSafePassVer(boolean safePassVer) {
        this.safePassVer = safePassVer;
    }

    public boolean isGardaVetVer() {
        return gardaVetVer;
    }

    public void setGardaVetVer(boolean gardaVetVer) {
        this.gardaVetVer = gardaVetVer;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public int getWorkRadius() {
        return workRadius;
    }

    public void setWorkRadius(int workRadius) {
        this.workRadius = workRadius;
    }

    public String getSafePassImage() {
        return safePassImage;
    }

    public void setSafePassImage(String safePassImage) {
        this.safePassImage = safePassImage;
    }

    public String getGardaVetImage() {
        return gardaVetImage;
    }

    public void setGardaVetImage(String gardaVetImage) {
        this.gardaVetImage = gardaVetImage;
    }

    public int getPunctuality() {
        return punctuality;
    }

    public void setPunctuality(int punctuality) {
        this.punctuality = punctuality;
    }

    public int getWorkQuality() {
        return workQuality;
    }

    public void setWorkQuality(int workQuality) {
        this.workQuality = workQuality;
    }

    public int getCommunication() {
        return communication;
    }

    public void setCommunication(int communication) {
        this.communication = communication;
    }

    public int getOverallCustomerSatisfaction() {
        return overallCustomerSatisfaction;
    }

    public void setOverallCustomerSatisfaction(int overallCustomerSatisfaction) {
        this.overallCustomerSatisfaction = overallCustomerSatisfaction;
    }

    public int getJobsCompleted() {
        return jobsCompleted;
    }

    public void setJobsCompleted(int jobsCompleted) {
        this.jobsCompleted = jobsCompleted;
    }

    public int getTotalEarned() {
        return totalEarned;
    }

    public void setTotalEarned(int totalEarned) {
        this.totalEarned = totalEarned;
    }
}
