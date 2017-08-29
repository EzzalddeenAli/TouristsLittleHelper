package com.insulardevelopment.touristslittlehelper.network.entities.route;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Маргарита on 29.08.2017.
 */

public class Distance {
    @SerializedName("value")
    int value;

    public Distance() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
