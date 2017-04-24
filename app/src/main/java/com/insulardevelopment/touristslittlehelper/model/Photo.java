package com.insulardevelopment.touristslittlehelper.model;


import java.io.Serializable;

/**
 * Класс, содержащий информацию о фото
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