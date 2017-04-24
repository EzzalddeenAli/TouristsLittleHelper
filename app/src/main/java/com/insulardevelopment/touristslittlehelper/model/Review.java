package com.insulardevelopment.touristslittlehelper.model;


/**
 * Класс, содержащий информацию об отзыве
 */

public class Review {

    private int id;
    private String authorName;
    private String authorURL;
    private String profilePhotoURL;
    private String time;
    private String text;
    private int rating;

    public Review() {
    }

    public Review(String authorName, String authorURL, String profilePhotoURL, String time, String text, int rating) {
        this.authorName = authorName;
        this.authorURL = authorURL;
        this.profilePhotoURL = profilePhotoURL;
        this.time = time;
        this.text = text;
        this.rating = rating;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorURL() {
        return authorURL;
    }

    public void setAuthorURL(String authorURL) {
        this.authorURL = authorURL;
    }

    public String getProfilePhotoURL() {
        return profilePhotoURL;
    }

    public void setProfilePhotoURL(String profilePhotoURL) {
        this.profilePhotoURL = profilePhotoURL;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
