package com.insulardevelopment.touristslittlehelper.model;

import android.arch.persistence.room.Relation;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, содержащий информацию о маршруте
 */
public class Route implements Serializable{

    private int id;
    @Relation(parentColumn = "id", entityColumn = "routeId")
    private List<Place> places;
    private String name;
    private double distance;
    private double time;
    private String city;
    private String encodedPoly;
    private boolean hasStartAndFinish;

    public Route() {
    }

    public Route(String name, double distance, double time, String city, String encodedPoly, boolean hasStartAndFinish) {
        this.name = name;
        this.distance = distance;
        this.time = time;
        this.city = city;
        this.encodedPoly = encodedPoly;
        this.hasStartAndFinish = hasStartAndFinish;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEncodedPoly() {
        return encodedPoly;
    }

    public void setEncodedPoly(String encodedPoly) {
        this.encodedPoly = encodedPoly;
    }
    public void setName(String name) {
        this.name = name;
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

    public List<Place> getPlaces() {
        return new ArrayList<>(places);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public boolean isHasStartAndFinish() {
        return hasStartAndFinish;
    }

    public void setHasStartAndFinish(boolean hasStartAndFinish) {
        this.hasStartAndFinish = hasStartAndFinish;
    }

    public List<LatLng> decodePoly() {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encodedPoly.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encodedPoly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encodedPoly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
