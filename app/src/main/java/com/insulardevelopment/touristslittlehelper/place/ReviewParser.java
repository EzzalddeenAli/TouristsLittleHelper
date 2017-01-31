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
    public static List<Review> parse(JSONObject jsonObject) {
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            review.setAuthorName(googleReviewJson.getString("author_name"));
            review.setAuthorURL(googleReviewJson.getString("author_url"));
            review.setProfilePhotoURL(googleReviewJson.getString("profile_photo_url"));
            review.setRating(googleReviewJson.getInt("rating"));
            review.setText(googleReviewJson.getString("text"));
            review.setTime(googleReviewJson.getInt("time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return review;
    }
}
