package com.insulardevelopment.touristslittlehelper.place;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.network.Http;
import com.insulardevelopment.touristslittlehelper.place.review.ReviewAdapter;
import com.insulardevelopment.touristslittlehelper.place.review.ReviewParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/*
*   Активити, содержащее информацию о месте
*/

public class PlaceActivity extends AppCompatActivity {

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private static final String CHOSEN_PLACE = "chosen place";
    private Place place;
    private TextView placeNameTextView, addressTextView, phoneNumberTextView, ratingTextView, webSiteTextView, workHoursTextView;

    public class GooglePlacesReadTask extends AsyncTask<Object, Integer, Place> {
        String googlePlacesData = null;
        JSONObject googlePlacesJson;

        @Override
        protected Place doInBackground(Object... inputObj) {
            try {
                String googlePlacesUrl = (String) inputObj[0];
                Http http = new Http();
                googlePlacesData = http.read(googlePlacesUrl);
                googlePlacesJson = new JSONObject(googlePlacesData);
                Place googlePlace = (new PlaceParser()).parse(googlePlacesJson);
                return googlePlace;
            } catch (Exception e) {
            }
            return new Place();
        }
    }

    public class PlaceParser{
        public Place parse(JSONObject json) {
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
                    if (googlePlaceJson.has("geometry")) place.setLatLng(new LatLng(googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                            googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lng")));
                    if (googlePlaceJson.has("photos")){
                        JSONArray jsonArray = googlePlaceJson.getJSONArray("photos");
                        int placesCount = jsonArray.length();
                        place.setPhotos(new ArrayList<String>());
                        for (int i = 0; i < placesCount; i++)  place.getPhotos().add(getPhoto((JSONObject) jsonArray.get(i)));
                    }
                    if (googlePlaceJson.has("reviews")) place.setReviews(ReviewParser.parse(googlePlaceJson.getJSONArray("reviews")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return place;
        }

        private String getPhoto(JSONObject json) throws JSONException {

            try {
                StringBuilder googlePhotoUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?");
                googlePhotoUrl.append("photoreference=" + json.getString("photo_reference"));
                googlePhotoUrl.append("&maxheight=1000&maxwidth=1000&key=AIzaSyCjnoH7MNT5iS90ZHk4cV_fYj3ZZTKKp_Y");
                String url = googlePhotoUrl.toString();
                return  url;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static void start(Context context, Place place){
        Intent intent = new Intent(context, PlaceActivity.class);
        intent.putExtra(CHOSEN_PLACE, place.getPlaceId());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        final String placeId = getIntent().getStringExtra(CHOSEN_PLACE);
        try{
            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
            googlePlacesUrl.append("language=ru&placeid=" + placeId + "&key=" + "AIzaSyCjnoH7MNT5iS90ZHk4cV_fYj3ZZTKKp_Y");
            PlaceActivity.GooglePlacesReadTask googlePlacesReadTask = new PlaceActivity.GooglePlacesReadTask();
            Object toPass = googlePlacesUrl.toString();
            place = googlePlacesReadTask.execute(toPass).get();
        }catch (SecurityException e){} catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        placeNameTextView = (TextView) findViewById(R.id.place_name_text_view);
        addressTextView = (TextView) findViewById(R.id.place_address_text_view);
        phoneNumberTextView = (TextView) findViewById(R.id.place_phone_number_text_view);
        ratingTextView = (TextView) findViewById(R.id.place_rating_text_view);
        webSiteTextView = (TextView) findViewById(R.id.place_website_text_view);
        workHoursTextView = (TextView) findViewById(R.id.place_work_hours_text_view);

        placeNameTextView.setText(place.getName());
        if (place.getFormattedAddress() != null ) {
            addressTextView.setText(place.getFormattedAddress());
        } else {
            findViewById(R.id.address_icon_iv).setVisibility(View.INVISIBLE);
        }
        if (place.getFormattedPhoneNumber() != null ) {
            phoneNumberTextView.setText(place.getFormattedPhoneNumber());
        } else {
            findViewById(R.id.phone_icon_iv).setVisibility(View.INVISIBLE);
        }
        if (place.getWebSite() != null ) {
            webSiteTextView.setText(place.getWebSite());
        } else {
            findViewById(R.id.website_icon_iv).setVisibility(View.INVISIBLE);
        }
        if (place.getWeekdayText() != null ) {
            workHoursTextView.setText(place.getWeekdayText());
        } else {
            findViewById(R.id.time_icon_iv).setVisibility(View.INVISIBLE);
        }
        ratingTextView.setText(String.valueOf(place.getRating()));
        LinearLayout layout = (LinearLayout) findViewById(R.id.photo_layout);
        if(place.getPhotos() != null && place.getPhotos().size() != 0) {
            int i = 0;
            for (String photoUrl : place.getPhotos()) {
                ImageView imageView = new ImageView(this);
                Glide.with(this)
                        .load(photoUrl)
                        .into(imageView);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                layout.addView(imageView);
                i++;
                if (i == 4) break;
            }
        } else {
            layout.setVisibility(View.INVISIBLE);
        }

        if (place.getReviews() != null){
            RecyclerView reviewRecycler = (RecyclerView) findViewById(R.id.reviews_recycler_view);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            ReviewAdapter adapter = new ReviewAdapter(place.getReviews());
            reviewRecycler.setLayoutManager(layoutManager);
            reviewRecycler.setAdapter(adapter);
        }

    }

}
