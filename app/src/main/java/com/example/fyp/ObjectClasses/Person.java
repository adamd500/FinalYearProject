package com.example.fyp.ObjectClasses;

import java.util.List;

public class Person {

    String name;
    String dob;
    String address;
    boolean idVerified;
    List<String> feedback;
    String location;
    String phoneNumber;
    String email;
    String password;
    String username;

    public Person(){

    }

    public Person(String name, String dob, String address, boolean idVerified, List<String> feedback, String location, String phoneNumber, String email, String password, String username) {
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.idVerified = idVerified;
        this.feedback = feedback;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isIdVerified() {
        return idVerified;
    }

    public void setIdVerified(boolean idVerified) {
        this.idVerified = idVerified;
    }

    public List<String> getFeedback() {
        return feedback;
    }

    public void setFeedback( List<String>feedback) {
        this.feedback = feedback;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
