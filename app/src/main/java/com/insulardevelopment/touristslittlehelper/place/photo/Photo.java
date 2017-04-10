package com.insulardevelopment.touristslittlehelper.place.photo;


import java.io.Serializable;

/**
 * Created by ritar on 04.04.2017.
 */

public class Photo implements Serializable{

    private int id;
    private String url;

    public Photo() {
    }

    public Photo(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}