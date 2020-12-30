package com.example.fyp.ObjectClasses;

import java.util.List;

public class Customer extends Person {

    public Customer(String name, String dob, String address, boolean idVerified, List<String> feedback, String location, String phoneNumber, String email, String password, String username) {
        super(name, dob, address, idVerified, feedback, location, phoneNumber, email, password, username);
    }

    public Customer() {

    }

}

