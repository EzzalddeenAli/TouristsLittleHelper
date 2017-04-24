package com.insulardevelopment.touristslittlehelper.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.insulardevelopment.touristslittlehelper.model.Place.DATABASE_NAME;

/**
 * Класс, содержащий информацию о месте
 */

@DatabaseTable(tableName = DATABASE_NAME)
public class Place implements Serializable{

    public static final String DATABASE_NAME = "places";

    @DatabaseField(generatedId = true, canBeNull = false, columnName = "id")
    private int id;
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(columnName = "address")
    private String formattedAddress;
    @DatabaseField(columnName = "phone_number")
    private String formattedPhoneNumber;
    @DatabaseField(columnName = "icon")
    private String icon;
    @DatabaseField(columnName = "place_id")
    private String placeId;
    @DatabaseField(columnName = "rating")
    private double rating;
    @DatabaseField(columnName = "weekday_text")
    private String weekdayText;
    @DatabaseField(columnName = "latitude")
    private double latitude;
    @DatabaseField(columnName = "longitude")
    private double longitude;
    @DatabaseField(columnName = "website")
    private String webSite;
    private ArrayList<Photo> photos;
    private List<Review> reviews;
    @DatabaseField(columnName = "route", foreign = true, foreignAutoCreate = true)
    private Route route;
    private boolean chosen = true;


    public Place() {
    }

    public Place(String name, String formattedAddress, String formattedPhoneNumber, String icon, String placeId, double rating, String weekdayText, double latitude, double longitude, String webSite, ArrayList<Photo> photos, List<Review> reviews, boolean chosen) {
        this.name = name;
        this.formattedAddress = formattedAddress;
        this.formattedPhoneNumber = formattedPhoneNumber;
        this.icon = icon;
        this.placeId = placeId;
        this.rating = rating;
        this.weekdayText = weekdayText;
        this.latitude = latitude;
        this.longitude = longitude;
        this.webSite = webSite;
        this.photos = photos;
        this.reviews = reviews;
        this.chosen = chosen;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos =photos;
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

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
