package com.insulardevelopment.touristslittlehelper.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Маргарита on 24.08.2017.
 */

public class ObjectResponse<T> {
    @SerializedName("result")
    private T result;

    public T getResult() {
        return result;
    }
}
