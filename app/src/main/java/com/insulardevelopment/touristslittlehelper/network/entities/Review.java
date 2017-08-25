package com.insulardevelopment.touristslittlehelper.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Маргарита on 24.08.2017.
 */

public class Review {
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("author_url")
    private String authorURL;
    @SerializedName("profile_photo_url")
    private String profilePhotoURL;
    @SerializedName("relative_time_description")
    private String time;
    @SerializedName("text")
    private String text;
    @SerializedName("rating")
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
