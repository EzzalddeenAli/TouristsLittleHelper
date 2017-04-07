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
import rx.functions.Action1;

/*
*   Активити, содержащее информацию о месте
*/

public class PlaceActivity extends AppCompatActivity {

    private static final String CHOSEN_PLACE = "chosen place";
    private Place place;
    private TextView placeNameTextView, addressTextView, phoneNumberTextView, ratingTextView, webSiteTextView, workHoursTextView;

    public class GooglePlacesReadTask extends AsyncTask<String, Integer, Place> {
        String googlePlacesData = null;
        JSONObject googlePlacesJson;

        @Override
        protected Place doInBackground(String... inputObj) {
            try {
                String googlePlacesUrl = inputObj[0];
                googlePlacesData = Http.read(googlePlacesUrl);
                Place googlePlace = (new PlaceParser()).getFullInfo(new JSONObject(googlePlacesData));
                return googlePlace;
            } catch (Exception e) {
            }
            return new Place();
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

        placeNameTextView = (TextView) findViewById(R.id.place_name_text_view);
        addressTextView = (TextView) findViewById(R.id.place_address_text_view);
        phoneNumberTextView = (TextView) findViewById(R.id.place_phone_number_text_view);
        ratingTextView = (TextView) findViewById(R.id.place_rating_text_view);
        webSiteTextView = (TextView) findViewById(R.id.place_website_text_view);
        workHoursTextView = (TextView) findViewById(R.id.place_work_hours_text_view);

        final String placeId = getIntent().getStringExtra(CHOSEN_PLACE);
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        googlePlacesUrl.append("language=ru&placeid=" + placeId + "&key=" + "AIzaSyCjnoH7MNT5iS90ZHk4cV_fYj3ZZTKKp_Y");
        PlaceActivity.GooglePlacesReadTask googlePlacesReadTask = new PlaceActivity.GooglePlacesReadTask();
        String request = googlePlacesUrl.toString();

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(request);
                subscriber.onCompleted();
            }
        });

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                try {
                    String googlePlacesData = Http.read(s);
                    Place googlePlace = (new PlaceParser()).getFullInfo(new JSONObject(s));
                    setContent(googlePlace);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        observable.subscribe(subscriber);

//        try{
//            place = googlePlacesReadTask.execute(request).get();
//            setContent(place);
//        }catch (SecurityException e){} catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

    }

    private void setContent(Place place){
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

        if (place.getReviews() != null){
            RecyclerView reviewRecycler = (RecyclerView) findViewById(R.id.reviews_recycler_view);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            ReviewAdapter adapter = new ReviewAdapter(place.getReviews());
            reviewRecycler.setLayoutManager(layoutManager);
            reviewRecycler.setAdapter(adapter);
        }
    }

}
