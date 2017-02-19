package com.insulardevelopment.touristslittlehelper.route;

import com.insulardevelopment.touristslittlehelper.place.Place;

import java.util.List;

/**
 * Класс, содержащий информацию о маршруте
 */

public class Route {
    private String name;
    private List<Place> places;
    private double distance;
    private double time;
    private String city;

    public Route() {
    }

    public Route(String city, String name, List<Place> places, double distance, double time) {
        this.city = city;
        this.name = name;
        this.places = places;
        this.distance = distance;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
