package com.insulardevelopment.touristslittlehelper.place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Маргарита on 18.01.2017.
 */

public class PlaceParser {
    public List<Place> parse(JSONObject jsonObject) {
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private List<Place> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<Place> placesList = new ArrayList<>();
        for (int i = 0; i < placesCount; i++) {
            try {
                placesList.add(getPlace((JSONObject) jsonArray.get(i)));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private Place getPlace(JSONObject googlePlaceJson) {
        Place place = new Place();
        try {
            place.setName(googlePlaceJson.getString("name"));
            place.setFormattedAddress(googlePlaceJson.getString("vicinity"));
            place.setIcon(googlePlaceJson.getString("icon"));
            place.setRating(googlePlaceJson.getDouble("rating"));
            place.setPlaceId(googlePlaceJson.getString("place_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
}
