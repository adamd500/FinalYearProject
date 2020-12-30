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

    public Professional() {

    }

    public Professional(String name, String dob, String address, boolean idVerified, List<String> feedback, String location, String phoneNumber, String email, String password, String username, boolean safePassVer, boolean gardaVetVer, List<Job> jobsCompleted, String trade, int workRadius) {
        super(name, dob, address, idVerified, feedback, location, phoneNumber, email, password, username);
        this.safePassVer = safePassVer;
        this.gardaVetVer = gardaVetVer;
        this.jobsCompleted = jobsCompleted;
        this.trade = trade;
        this.workRadius = workRadius;
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
}
