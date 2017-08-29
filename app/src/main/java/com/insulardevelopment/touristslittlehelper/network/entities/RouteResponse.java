package com.insulardevelopment.touristslittlehelper.network.entities;

import com.google.gson.annotations.SerializedName;
import com.insulardevelopment.touristslittlehelper.network.entities.route.Route;

import java.util.List;

/**
 * Created by Маргарита on 29.08.2017.
 */

public class RouteResponse {
    @SerializedName("routes")
    private List<Route> result;

    public List<Route> getResult() {
        return result;
    }
}
