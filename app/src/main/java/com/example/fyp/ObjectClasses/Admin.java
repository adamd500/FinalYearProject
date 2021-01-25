package com.example.fyp.ObjectClasses;

public class Admin {

    String adminId;
    String name;
    String email;

    public Admin(String adminId, String name, String email) {
        this.adminId = adminId;
        this.name = name;
        this.email = email;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
