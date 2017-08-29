package com.insulardevelopment.touristslittlehelper.network.entities.route;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Маргарита on 29.08.2017.
 */

public class Leg {
    @SerializedName("distance")
    private Distance distance;

    @SerializedName("duration")
    private Duration duration;

    public Leg() {
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
