package com.insulardevelopment.touristslittlehelper.view.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.insulardevelopment.touristslittlehelper.view.AbstractActivity;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.model.Photo;
import com.insulardevelopment.touristslittlehelper.view.adapters.ReviewAdapter;
import com.insulardevelopment.touristslittlehelper.view.viewmodel.PlaceViewModel;

import java.util.ArrayList;
/*
*   Активити, содержащее информацию о месте
*/

public class PlaceActivity extends AbstractActivity {

    private static final String CHOSEN_PLACE = "chosen place";
    private TextView addressTextView, phoneNumberTextView, webSiteTextView, workHoursTextView;
    private RatingBar ratingBar;
    private String placeId;

    private PlaceViewModel placeViewModel;

    public static void start(Context context, Place place){
        Intent intent = new Intent(context, PlaceActivity.class);
        intent.putExtra(CHOSEN_PLACE, place.getPlaceId());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        placeViewModel = getViewModel(PlaceViewModel.class);

        initViews();
        placeId = getIntent().getStringExtra(CHOSEN_PLACE);
        placeViewModel.getPlace(placeId, getString(R.string.google_api_key))
                .observe(this, this::setContent);
    }

    private void setContent(Place place){
        Toolbar toolbar = findViewById(R.id.place_toolbar);
        toolbar.setTitle(place.getName());

        if (place.getFormattedAddress() != null ) {
            addressTextView.setText(place.getFormattedAddress());
        } else {
            findViewById(R.id.address_icon_iv).setVisibility(View.GONE);
        }
        if (place.getFormattedPhoneNumber() != null ) {
            phoneNumberTextView.setText(place.getFormattedPhoneNumber());
        } else {
            findViewById(R.id.phone_icon_iv).setVisibility(View.GONE);
        }
        if (place.getWebSite() != null ) {
            webSiteTextView.setText(place.getWebSite());
        } else {
            findViewById(R.id.website_icon_iv).setVisibility(View.GONE);
        }
        if (place.getWeekdayText() != null) {
            workHoursTextView.setText(place.getWeekdayText());
        } else {
            findViewById(R.id.time_icon_iv).setVisibility(View.GONE);
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
            photoIb.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.tourist_icon));
        }

        if (place.getReviews() != null){
            RecyclerView reviewRecycler = findViewById(R.id.reviews_recycler_view);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            ReviewAdapter adapter = new ReviewAdapter(place.getReviews());
            reviewRecycler.setLayoutManager(layoutManager);
            reviewRecycler.setAdapter(adapter);
        }
    }

    private void initViews(){
        addressTextView = findViewById(R.id.place_address_text_view);
        phoneNumberTextView = findViewById(R.id.place_phone_number_text_view);
        webSiteTextView = findViewById(R.id.place_website_text_view);
        workHoursTextView = findViewById(R.id.place_work_hours_text_view);
        ratingBar = findViewById(R.id.place_rating_bar);
    }

}
