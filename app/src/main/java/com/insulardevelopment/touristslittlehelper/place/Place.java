package com.insulardevelopment.touristslittlehelper.place;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Маргарита on 20.11.2016.
 */

public class Place{
    private String name;
    private String formattedAddress;
    private String formattedPhoneNumber;
    private String icon;
    private String placeId;
    private double rating;
    private String weekdayText;
    private LatLng latLng;
    private String webSite;
    private List<Drawable> photos;
    private List<Review> reviews;


    public Place() {
    }

    public Place(String name, String formattedAddress, String formattedPhoneNumber, String icon, String placeId, double rating, String weekdayText, LatLng latLng, String webSite, List<Drawable> photos, List<Review> reviews) {
        this.name = name;
        this.formattedAddress = formattedAddress;
        this.formattedPhoneNumber = formattedPhoneNumber;
        this.icon = icon;
        this.placeId = placeId;
        this.rating = rating;
        this.weekdayText = weekdayText;
        this.latLng = latLng;
        this.webSite = webSite;
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

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
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

    public List<Drawable> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Drawable> photos) {
        this.photos = photos;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }
}
