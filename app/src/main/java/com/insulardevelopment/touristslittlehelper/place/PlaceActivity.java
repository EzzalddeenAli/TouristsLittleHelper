package com.insulardevelopment.touristslittlehelper.place;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.network.Http;
import com.insulardevelopment.touristslittlehelper.place.photo.Photo;
import com.insulardevelopment.touristslittlehelper.place.photo.PhotoActivity;
import com.insulardevelopment.touristslittlehelper.place.review.ReviewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*
*   Активити, содержащее информацию о месте
*/

public class PlaceActivity extends AppCompatActivity {

    private static final String CHOSEN_PLACE = "chosen place";
    private TextView addressTextView, phoneNumberTextView, webSiteTextView, workHoursTextView;
    private RatingBar ratingBar;

    public static void start(Context context, Place place){
        Intent intent = new Intent(context, PlaceActivity.class);
        intent.putExtra(CHOSEN_PLACE, place.getPlaceId());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        addressTextView = (TextView) findViewById(R.id.place_address_text_view);
        phoneNumberTextView = (TextView) findViewById(R.id.place_phone_number_text_view);
        webSiteTextView = (TextView) findViewById(R.id.place_website_text_view);
        workHoursTextView = (TextView) findViewById(R.id.place_work_hours_text_view);
        ratingBar = (RatingBar) findViewById(R.id.place_rating_bar);

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
        Toolbar toolbar = (Toolbar)findViewById(R.id.place_toolbar);
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
        ratingBar.setRating((float) place.getRating());
        ImageView photoIb = (ImageView) findViewById(R.id.place_photo_iv);
        if (place.getPhotos() != null && place.getPhotos().size() != 0) {
            Glide.with(this)
                    .load(place.getPhotos().get(0).getUrl())
                    .into(photoIb);
            photoIb.setOnClickListener(v -> {
                PhotoActivity.start(this, (ArrayList<Photo>) place.getPhotos());
            });
        } else {
            photoIb.setImageDrawable(getResources().getDrawable(R.drawable.tourist_icon));
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
