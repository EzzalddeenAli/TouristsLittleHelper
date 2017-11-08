package com.insulardevelopment.touristslittlehelper.network.entities.Place;

import com.google.gson.annotations.SerializedName;
import com.insulardevelopment.touristslittlehelper.model.Photo;
import com.insulardevelopment.touristslittlehelper.model.Review;
import com.insulardevelopment.touristslittlehelper.model.Route;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, содержащий информацию о месте
 */
public class Place implements Serializable{

    @SerializedName("name")
    private String name;

    @SerializedName(value = "formatted_address", alternate = "vicinity")
    private String formattedAddress;

    @SerializedName("formatted_phone_number")
    private String formattedPhoneNumber;

    @SerializedName("icon")
    private String icon;

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("rating")
    private double rating;

    @SerializedName("opening_hours")
    private OpeningHours openingHours;

    @DatabaseField(columnName = "website")
    @SerializedName("website")
    private String webSite;

    @SerializedName("geometry")
    private Geometry geometry;

    @SerializedName("reviews")
    private ArrayList<Review> reviews;

    @SerializedName("photos")
    private ArrayList<Photo> photos;

    private Route route;

    private boolean chosen = true;

    public Place() {
    }

    public Place(String name) {
        this.name = name;
    }

    public Place(String name, String formattedAddress, String formattedPhoneNumber, String icon, String placeId, double rating, OpeningHours openingHours, String webSite, Geometry geometry, ArrayList<Review> reviews, ArrayList<Photo> photos) {
        this.name = name;
        this.formattedAddress = formattedAddress;
        this.formattedPhoneNumber = formattedPhoneNumber;
        this.icon = icon;
        this.placeId = placeId;
        this.rating = rating;
        this.openingHours = openingHours;
        this.webSite = webSite;
        this.geometry = geometry;
        this.reviews = reviews;
        this.photos = photos;
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

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public com.insulardevelopment.touristslittlehelper.model.Place toModelPlace(){
        com.insulardevelopment.touristslittlehelper.model.Place place= new com.insulardevelopment.touristslittlehelper.model.Place();
        place.setName(name);
        place.setChosen(chosen);
        place.setFormattedAddress(formattedAddress);
        place.setFormattedPhoneNumber(formattedPhoneNumber);
        place.setIcon(icon);
        place.setLatitude(geometry.getLocation().getLatitude());
        place.setLongitude(geometry.getLocation().getLongitude());
        place.setPhotos(photos);
        place.setReviews(reviews);
        place.setPlaceId(placeId);
        place.setRating(rating);
        place.setWebSite(webSite);
        String weekdayText = "";
        if (getOpeningHours() != null && getOpeningHours().getWeekdayText() != null) {
            for (String day : getOpeningHours().getWeekdayText()) {
                weekdayText += (day + "\n");
            }
        }
        place.setWeekdayText(weekdayText);
        return place;
    }


}
