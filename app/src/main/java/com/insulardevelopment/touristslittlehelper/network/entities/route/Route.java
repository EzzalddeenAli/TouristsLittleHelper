package com.insulardevelopment.touristslittlehelper.network.entities.route;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Маргарита on 29.08.2017.
 */

public class Route {

    @SerializedName("overview_polyline")
    private OverviewPolyline overviewPolyline;

    @SerializedName("legs")
    private List<Leg> legs;

    public Route() {
    }

    public OverviewPolyline getOverviewPolyline() {
        return overviewPolyline;
    }

    public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public com.insulardevelopment.touristslittlehelper.model.Route toModelRoute(){
        com.insulardevelopment.touristslittlehelper.model.Route route = new com.insulardevelopment.touristslittlehelper.model.Route();
        route.setEncodedPoly(overviewPolyline.getPoints());
        int distance = 0, time = 0;
        for (Leg leg: getLegs()) {
            distance += leg.getDistance().getValue();
            time += leg.getDuration().getValue();
        }
        route.setDistance(distance);
        route.setTime(time);
        return route;
    }
}
