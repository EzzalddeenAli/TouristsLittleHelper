package com.insulardevelopment.touristslittlehelper.network.entities.Place;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Маргарита on 29.08.2017.
 */

public class Location implements Serializable {

    @SerializedName("lat")
    private double latitude;

    @SerializedName("lng")
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {
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
}