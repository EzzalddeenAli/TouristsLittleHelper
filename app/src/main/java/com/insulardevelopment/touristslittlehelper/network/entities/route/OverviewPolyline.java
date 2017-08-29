package com.insulardevelopment.touristslittlehelper.network.entities.route;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Маргарита on 29.08.2017.
 */

public class OverviewPolyline {

    @SerializedName("points")
    private String points;

    public OverviewPolyline() {
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
