package com.insulardevelopment.touristslittlehelper.util;

import com.insulardevelopment.touristslittlehelper.model.Place;

import java.util.List;

/**
 * Created by Маргарита on 07.11.2017.
 */

public class GeoUtil {
    public static void changePlacesOrder(List<Place> places, boolean hasStartAndFinish) {
        Place startPlace = null, finishPlace = null;
        if (hasStartAndFinish) {
            for (Place place : places) {
                if (place.getName().equals(Place.START_PLACE)) {
                    startPlace = place;
                }
                if (place.getName().equals(Place.FINISH_PLACE)) {
                    finishPlace = place;
                }
            }
            if (finishPlace == null) {
                finishPlace = findMostFarPlace(startPlace, places);
            }
            if (startPlace == null) {
                startPlace = findMostFarPlace(finishPlace, places);
            }
        } else {
            double max = 0;
            for (Place place1 : places) {
                for (Place place2 : places) {
                    if (place1 != place2) {
                        double distance = findDistance(place1, place2);
                        if (distance > max) {
                            max = distance;
                            startPlace = place1;
                            finishPlace = place2;
                        }
                    }
                }
            }
        }
        places.remove(startPlace);
        places.remove(finishPlace);
        places.add(0, startPlace);
        places.add(finishPlace);
    }

    public static Place findMostFarPlace(Place place1, List<Place> places) {
        Place place2 = null;
        double max = 0;
        for (Place place : places) {
            if (place1 != place) {
                double distance = findDistance(place1, place);
                if (distance > max) {
                    max = distance;
                    place2 = place;
                }
            }
        }
        return place2;
    }

    private static double findDistance(Place place1, Place place2) {
        final int earthRadius = 6371;
        double latDistance = Math.toRadians(place2.getLatitude() - place1.getLatitude());
        double lonDistance = Math.toRadians(place2.getLongitude() - place1.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(place1.getLatitude())) * Math.cos(Math.toRadians(place2.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c * 1000;
    }
}
