package com.insulardevelopment.touristslittlehelper;

/**
 * Created by Маргарита on 20.11.2016.
 */

public class PlaceEntity {
    private String name;
    private String address;

    public PlaceEntity() {
    }

    public PlaceEntity(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
