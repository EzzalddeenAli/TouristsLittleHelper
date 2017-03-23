package com.insulardevelopment.touristslittlehelper;

import com.insulardevelopment.touristslittlehelper.place.Place;

import java.util.List;

/**
 * Created by Маргарита on 22.03.2017.
 */


public class ChosenPlaces {
    private static ChosenPlaces instance = new ChosenPlaces();
    private List<Place> places;
    private ChosenPlaces(){}

    public static ChosenPlaces getInstance() {
        return instance;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
