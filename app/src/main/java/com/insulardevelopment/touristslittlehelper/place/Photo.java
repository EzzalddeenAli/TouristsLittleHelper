package com.insulardevelopment.touristslittlehelper.place;

/**
 * Created by Маргарита on 25.12.2016.
 */

public class Photo {
    private int height;
    private int width;
    private String photoReference;

    public Photo(int height, int width, String photoReference) {
        this.height = height;
        this.width = width;
        this.photoReference = photoReference;
    }

    public Photo() {
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }
}
