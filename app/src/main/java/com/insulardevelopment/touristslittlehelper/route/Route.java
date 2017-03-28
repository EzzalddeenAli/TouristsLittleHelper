package com.insulardevelopment.touristslittlehelper.route;

import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.place.Place;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, содержащий информацию о маршруте
 */
@DatabaseTable(tableName = "routes")
public class Route {

    @DatabaseField(generatedId = true, canBeNull = false, columnName = "id")
    private int id;
    @DatabaseField(dataType = DataType.STRING, columnName = "name")
    private String name;
    @DatabaseField(dataType = DataType.DOUBLE, columnName = "distance")
    private double distance;
    @DatabaseField(dataType = DataType.DOUBLE, columnName = "time")
    private double time;
    @DatabaseField(dataType = DataType.STRING, columnName = "city")
    private String city;
    @DatabaseField(dataType = DataType.STRING, columnName = "encoded_poly")
    private String encodedPoly;



    public Route() {
    }

    public Route(String city, String name, double distance, double time, String encodedPoly) {
        this.name = name;
        this.distance = distance;
        this.time = time;
        this.city = city;
        this.encodedPoly = encodedPoly;
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

    public static List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
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
