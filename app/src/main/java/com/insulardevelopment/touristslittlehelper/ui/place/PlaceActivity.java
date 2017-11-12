package com.insulardevelopment.touristslittlehelper.ui.place;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Photo;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.ui.AbstractActivity;

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

        placeViewModel.getErrorLiveData().observe(this, s -> {
            Snackbar.make(findViewById(R.id.container), getString(R.string.error), Snackbar.LENGTH_LONG).show();
        });
    }

    private void setContent(Place place){
        Toolbar toolbar = findViewById(R.id.place_toolbar);
        toolbar.setTitle(place.getName());

        if (place.getFormattedAddress() != null && !place.getFormattedAddress().isEmpty()) {
            addressTextView.setVisibility(View.VISIBLE);
            addressTextView.setText(place.getFormattedAddress());
        }
        if (place.getFormattedPhoneNumber() != null && !place.getFormattedPhoneNumber().isEmpty()) {
            phoneNumberTextView.setVisibility(View.VISIBLE);
            phoneNumberTextView.setText(place.getFormattedPhoneNumber());
        }
        if (place.getWebSite() != null && !place.getWebSite().isEmpty()) {
            webSiteTextView.setVisibility(View.VISIBLE);
            webSiteTextView.setText(place.getWebSite());
        }
        if (place.getWeekdayText() != null && !place.getWeekdayText().isEmpty()) {
            workHoursTextView.setVisibility(View.VISIBLE);
            workHoursTextView.setText(place.getWeekdayText());
        }
        ratingBar.setRating((float) place.getRating());
        ImageView photoIb = findViewById(R.id.place_photo_iv);
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
