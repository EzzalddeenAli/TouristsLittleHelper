package com.insulardevelopment.touristslittlehelper.database;

/**
 * Created by rita on 07.11.2017.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.insulardevelopment.touristslittlehelper.model.Route;

import java.io.Serializable;

@Entity(tableName = "route")
public class RouteEntity implements Serializable {

    @PrimaryKey
    private int id;
    private String name;
    private double distance;
    private double time;
    private String city;
    private String encodedPoly;
    private boolean hasStartAndFinish;

    public RouteEntity() {
    }

    public RouteEntity(Route route) {
        this.id = route.getId();
        this.name = route.getName();
        this.distance = route.getDistance();
        this.time = route.getTime();
        this.city = route.getCity();
        this.encodedPoly = route.getEncodedPoly();
        this.hasStartAndFinish = route.isHasStartAndFinish();
    }

    public RouteEntity(String name, double distance, double time, String city, String encodedPoly, boolean hasStartAndFinish) {
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

    public void setId(int id) {
        this.id = id;
    }

    public boolean isHasStartAndFinish() {
        return hasStartAndFinish;
    }

    public void setHasStartAndFinish(boolean hasStartAndFinish) {
        this.hasStartAndFinish = hasStartAndFinish;
    }

}

