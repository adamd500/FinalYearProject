package com.example.fyp.ObjectClasses;

import java.util.List;

public class Customer extends Person {

    String idImage;
    String selfieImage;

    public Customer(String name, String dob, String address, boolean idVerified, List<String> feedback, String location, String phoneNumber, String email, String password, String username, String idImage, String selfieImage) {
        super(name, dob, address, idVerified, feedback, location, phoneNumber, email, password, username);
        this.idImage = idImage;
        this.selfieImage = selfieImage;
    }

    public Customer() {

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
}

