package com.insulardevelopment.touristslittlehelper.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Класс, содержащий информацию о фото
 */

public class Photo implements Serializable{

    private int id;
    @SerializedName("photo_reference")
    private String photoRef;

    public Photo() {
    }

    public String getPhotoRef() {
        return photoRef;
    }

    public void setPhotoRef(String photoRef) {
        this.photoRef = photoRef;
    }

    public String getUrl(){
        StringBuilder googlePhotoUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?");
        googlePhotoUrl.append("maxwidth=700");
        googlePhotoUrl.append("&photoreference=" + photoRef);
        googlePhotoUrl.append("&key=AIzaSyCjnoH7MNT5iS90ZHk4cV_fYj3ZZTKKp_Y");
        return googlePhotoUrl.toString();
    }
}