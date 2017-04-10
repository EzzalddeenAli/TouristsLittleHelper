package com.insulardevelopment.touristslittlehelper.place;

import com.insulardevelopment.touristslittlehelper.place.photo.Photo;
import com.insulardevelopment.touristslittlehelper.place.review.ReviewParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Маргарита on 18.01.2017.
 */

public class PlaceParser {
    public static List<Place> getSomeInfo(JSONObject jsonObject) {
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private static List<Place> getPlaces(JSONArray jsonArray) {
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

    private static Place getPlace(JSONObject googlePlaceJson) {
        Place place = new Place();
        try {
            if(googlePlaceJson.has("name")) place.setName(googlePlaceJson.getString("name"));
            if(googlePlaceJson.has("vicinity")) place.setFormattedAddress(googlePlaceJson.getString("vicinity"));
            if(googlePlaceJson.has("icon")) place.setIcon(googlePlaceJson.getString("icon"));
            if(googlePlaceJson.has("rating")) place.setRating(googlePlaceJson.getDouble("rating"));
            if(googlePlaceJson.has("place_id")) place.setPlaceId(googlePlaceJson.getString("place_id"));
            if(googlePlaceJson.has("geometry")) {
                place.setLatitude(googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                place.setLongitude(googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
    public static Place getFullInfo (JSONObject json) {
        Place place = new Place();
        try {
            if (json.has("result")) {
                JSONObject googlePlaceJson = json.getJSONObject("result");
                if (googlePlaceJson.has("name")) place.setName(googlePlaceJson.getString("name"));
                if (googlePlaceJson.has("formatted_address")) place.setFormattedAddress(googlePlaceJson.getString("formatted_address"));
                if (googlePlaceJson.has("formatted_phone_number")) place.setFormattedPhoneNumber(googlePlaceJson.getString("formatted_phone_number"));
                if (googlePlaceJson.has("icon")) place.setIcon(googlePlaceJson.getString("icon"));
                if (googlePlaceJson.has("rating")) place.setRating(googlePlaceJson.getDouble("rating"));
                if (googlePlaceJson.has("place_id")) place.setPlaceId(googlePlaceJson.getString("place_id"));
                if (googlePlaceJson.has("website")) place.setWebSite(googlePlaceJson.getString("website"));
                if (googlePlaceJson.has("opening_hours")) {
                    JSONObject jsonObject = googlePlaceJson.getJSONObject("opening_hours");
                    if(jsonObject.has("weekday_text")) {
                        String weekdatText = jsonObject.getString("weekday_text").replace("\",\"", "\n");
                        place.setWeekdayText(weekdatText.substring(2, weekdatText.length() - 2));
                    }
                }
                if (googlePlaceJson.has("geometry")) {
                    place.setLatitude(googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                    place.setLongitude(googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                }
                if (googlePlaceJson.has("photos")){
                    JSONArray jsonArray = googlePlaceJson.getJSONArray("photos");
                    int placesCount = jsonArray.length();
                    place.setPhotos(new ArrayList<Photo>());
                    for (int i = 0; i < placesCount; i++)  place.getPhotos().add(getPhoto((JSONObject) jsonArray.get(i)));
                }
                if (googlePlaceJson.has("reviews")) place.setReviews(ReviewParser.parse(googlePlaceJson.getJSONArray("reviews")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }

    private static Photo getPhoto(JSONObject json) throws JSONException {

        try {
            StringBuilder googlePhotoUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?");
            googlePhotoUrl.append("photoreference=" + json.getString("photo_reference"));
            googlePhotoUrl.append("&maxheight=1000&maxwidth=1000&key=AIzaSyCjnoH7MNT5iS90ZHk4cV_fYj3ZZTKKp_Y");
            String url = googlePhotoUrl.toString();
            return  new Photo(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
