package com.insulardevelopment.touristslittlehelper.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Маргарита on 25.08.2017.
 */

public class ArrayResponse<T> {
    @SerializedName("results")
    private List<T> result;

    public List<T> getResult() {
        return result;
    }
}