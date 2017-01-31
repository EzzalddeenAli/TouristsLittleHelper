package com.insulardevelopment.touristslittlehelper.place;

import java.util.List;

/**
 * Created by Маргарита on 20.11.2016.
 */

public class Place {
    private String name;
    private String formattedAddress;
    private String formattedPhoneNumber;
    private String icon;
    private String placeId;
    private double rating;
    private String weekdayText;
    private List<Photo> photos;
    private List<Review> reviews;


    public Place() {
    }

    public Place(String name, String formattedAddress, String formattedPhoneNumber, String icon, String placeId, double rating, String weekdayText, List<Photo> photos, List<Review> reviews) {
        this.name = name;
        this.formattedAddress = formattedAddress;
        this.formattedPhoneNumber = formattedPhoneNumber;
        this.icon = icon;
        this.placeId = placeId;
        this.rating = rating;
        this.weekdayText = weekdayText;
        this.photos = photos;
        this.reviews = reviews;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(String weekdayText) {
        this.weekdayText = weekdayText;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

}
