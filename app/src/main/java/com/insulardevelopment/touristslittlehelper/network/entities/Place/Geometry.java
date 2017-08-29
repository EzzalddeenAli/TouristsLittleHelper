package com.insulardevelopment.touristslittlehelper.network.entities.Place;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Created by Маргарита on 29.08.2017.
 */

public class Geometry implements Serializable {

    @SerializedName("location")
    private Location location;

    public Geometry() {
    }

    public Geometry(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}