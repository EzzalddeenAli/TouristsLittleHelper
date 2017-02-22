package com.insulardevelopment.touristslittlehelper.place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Маргарита on 18.01.2017.
 */

public class ReviewParser {
    public static List<Review> parse(JSONArray jsonObject) {
        JSONArray jsonArray = jsonObject;
        return getReviews(jsonArray);
    }

    private static List<Review> getReviews(JSONArray jsonArray) {
        int reviewsCount = jsonArray.length();
        List<Review> reviewsList = new ArrayList<>();
        for (int i = 0; i < reviewsCount; i++) {
            try {
                reviewsList.add(getReview((JSONObject) jsonArray.get(i)));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return reviewsList;
    }

    private static Review getReview(JSONObject googleReviewJson) {
        Review review = new Review();
        try {
            if(googleReviewJson.has("author_name")) review.setAuthorName(googleReviewJson.getString("author_name"));
            if(googleReviewJson.has("author_url")) review.setAuthorURL(googleReviewJson.getString("author_url"));
            if(googleReviewJson.has("profile_photo_url")) review.setProfilePhotoURL(googleReviewJson.getString("profile_photo_url"));
            if(googleReviewJson.has("author_name")) review.setRating(googleReviewJson.getInt("rating"));
            if(googleReviewJson.has("text")) review.setText(googleReviewJson.getString("text"));
            if(googleReviewJson.has("relative_time_description")) review.setTime(googleReviewJson.getString("relative_time_description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return review;
    }
}
