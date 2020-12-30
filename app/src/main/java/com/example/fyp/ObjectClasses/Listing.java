package com.example.fyp.ObjectClasses;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
@Parcel
public class Listing {

    boolean active;
    ArrayList<String> photos;
    String description;
    String location;
    String tradeSector;
    String customerUsername;
    String listingId;

    public Listing(boolean active, ArrayList<String> photos, String description, String location, String tradeSector, String customerUsername, String listingId) {
        this.active = active;
        this.photos = photos;
        this.description = description;
        this.location = location;
        this.tradeSector = tradeSector;
        this.customerUsername = customerUsername;
        this.listingId = listingId;
    }

    public Listing() {

    }
    public Listing(boolean active) {
        this.active=active;
    }

    public void addPhoto(String photo){
        photos.add(photo);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTradeSector() {
        return tradeSector;
    }

    public void setTradeSector(String tradeSector) {
        this.tradeSector = tradeSector;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }
}
