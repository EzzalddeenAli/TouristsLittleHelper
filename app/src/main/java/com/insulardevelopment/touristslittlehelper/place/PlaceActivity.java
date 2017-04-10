package com.insulardevelopment.touristslittlehelper.place;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.network.Http;
import com.insulardevelopment.touristslittlehelper.place.review.ReviewAdapter;
import com.insulardevelopment.touristslittlehelper.place.review.ReviewParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/*
*   Активити, содержащее информацию о месте
*/

public class PlaceActivity extends AppCompatActivity {

    private static final String CHOSEN_PLACE = "chosen place";
    private Place place;
    private TextView placeNameTextView, addressTextView, phoneNumberTextView, ratingTextView, webSiteTextView, workHoursTextView;

    public static void start(Context context, Place place){
        Intent intent = new Intent(context, PlaceActivity.class);
        intent.putExtra(CHOSEN_PLACE, place.getPlaceId());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp);

     //   placeNameTextView = (TextView) findViewById(R.id.place_name_text_view);
        addressTextView = (TextView) findViewById(R.id.place_address_text_view);
        phoneNumberTextView = (TextView) findViewById(R.id.place_phone_number_text_view);
        ratingTextView = (TextView) findViewById(R.id.place_rating_text_view);
        webSiteTextView = (TextView) findViewById(R.id.place_website_text_view);
        workHoursTextView = (TextView) findViewById(R.id.place_work_hours_text_view);

        final String placeId = getIntent().getStringExtra(CHOSEN_PLACE);
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        googlePlacesUrl.append("language=ru&placeid=" + placeId + "&key=" + "AIzaSyCjnoH7MNT5iS90ZHk4cV_fYj3ZZTKKp_Y");
        String request = googlePlacesUrl.toString();

        Observable.just(request)
                .map(s -> {
                    try {
                        return Http.read(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .map(s -> {
                    try {
                        return PlaceParser.getFullInfo(new JSONObject(s));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(p -> setContent(p));

    }

    private void setContent(Place place){
      //  placeNameTextView.setText(place.getName());
        Toolbar toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        toolbar.setTitle(place.getName());
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
            for (Photo photoUrl : place.getPhotos()) {
                ImageView imageView = new ImageView(this);
                Glide.with(this)
                        .load(photoUrl.getUrl())
                        .into(imageView);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                layout.addView(imageView);
                i++;
                if (i == 4) break;
            }
        } else {
            layout.setVisibility(View.INVISIBLE);
        }
//
//        Glide.with(this)
//                .load(place.getPhotos().get(0).getUrl())
//                .into((ImageView) findViewById(R.id.main_backdrop));

        if (place.getReviews() != null){
            RecyclerView reviewRecycler = (RecyclerView) findViewById(R.id.reviews_recycler_view);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            ReviewAdapter adapter = new ReviewAdapter(place.getReviews());
            reviewRecycler.setLayoutManager(layoutManager);
            reviewRecycler.setAdapter(adapter);
        }
    }

}
