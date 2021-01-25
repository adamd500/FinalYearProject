package com.example.fyp.ObjectClasses;

import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ObjectClasses.Person;

import java.util.List;

public class Professional extends Person {

    boolean safePassVer;
    boolean gardaVetVer;
    List<Job> jobsCompleted;
    String trade;
    int workRadius;
    String idImage;
    String selfieImage;
    String safePassImage;
    String gardaVetImage;

    public Professional() {

    }


    public Professional(String name, String dob, String address, boolean idVerified, List<String> feedback, String location, String phoneNumber, String email, String password, String username, boolean safePassVer, boolean gardaVetVer,
                        List<Job> jobsCompleted, String trade, int workRadius, String idImage, String selfieImage, String safePassImage, String gardaVetImage) {
        super(name, dob, address, idVerified, feedback, location, phoneNumber, email, password, username);
        this.safePassVer = safePassVer;
        this.gardaVetVer = gardaVetVer;
        this.jobsCompleted = jobsCompleted;
        this.trade = trade;
        this.workRadius = workRadius;
        this.idImage = idImage;
        this.selfieImage = selfieImage;
        this.safePassImage = safePassImage;
        this.gardaVetImage = gardaVetImage;
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

    public List<Job> getJobsCompleted() {
        return jobsCompleted;
    }

    public void setJobsCompleted(List<Job> jobsCompleted) {
        this.jobsCompleted = jobsCompleted;
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

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getSelfieImage() {
        return selfieImage;
    }

    public void setSelfieImage(String selfieImage) {
        this.selfieImage = selfieImage;
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
}
