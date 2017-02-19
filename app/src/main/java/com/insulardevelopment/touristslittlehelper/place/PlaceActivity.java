package com.insulardevelopment.touristslittlehelper.place;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.ChoosePlacesActivity;
import com.insulardevelopment.touristslittlehelper.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/*
*   Активити, содержащее информацию о месте
*/

public class PlaceActivity extends AppCompatActivity {

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private static final String CHOSEN_PLACE = "chosen place";
    private Place place;
    private TextView placeNameTextView, addressTextView, phoneNumberTextView, ratingTextView, webSiteTextView;

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
                    if (googlePlaceJson.has("geometry")) place.setLatLng(new LatLng(googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                            googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lng")));
                    if (googlePlaceJson.has("photos")){
                        JSONArray jsonArray = googlePlaceJson.getJSONArray("photos");
                        int placesCount = jsonArray.length();
                        place.setPhotos(new ArrayList<Drawable>());
                        for (int i = 0; i < placesCount; i++)  place.getPhotos().add(getPhoto((JSONObject) jsonArray.get(i)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return place;
        }

        private Drawable getPhoto(JSONObject json) throws JSONException {

            try {
                StringBuilder googlePhotoUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?");
                googlePhotoUrl.append("photoreference=" + json.getString("photo_reference"));
                googlePhotoUrl.append("&maxheight=1000&maxwidth=1000&key=AIzaSyCjnoH7MNT5iS90ZHk4cV_fYj3ZZTKKp_Y");
                String url = googlePhotoUrl.toString();
                InputStream is = (InputStream) new URL(url).getContent();
                return Drawable.createFromStream(is, "src name");
            } catch (IOException e) {
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
            googlePlacesUrl.append("placeid=" + placeId + "&key=" + "AIzaSyCjnoH7MNT5iS90ZHk4cV_fYj3ZZTKKp_Y");
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

        placeNameTextView.setText(place.getName());
        addressTextView.setText(place.getFormattedAddress());
        phoneNumberTextView.setText(place.getFormattedPhoneNumber());
        ratingTextView.setText(String.valueOf(place.getRating()));
        webSiteTextView.setText(place.getWebSite());

        final View thumb1View = findViewById(R.id.thumb_button_1);
        ((ImageButton) thumb1View).setImageDrawable(place.getPhotos().get(0));
        thumb1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(thumb1View, place.getPhotos().get(0));
            }
        });

        final View thumb2View = findViewById(R.id.thumb_button_2);
        ((ImageButton) thumb2View).setImageDrawable(place.getPhotos().get(1));
        thumb2View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(thumb2View, place.getPhotos().get(1));
            }
        });

        final View thumb3View = findViewById(R.id.thumb_button_3);
        ((ImageButton) thumb3View).setImageDrawable(place.getPhotos().get(2));
        thumb3View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(thumb3View, place.getPhotos().get(2));
            }
        });

        final View thumb4View = findViewById(R.id.thumb_button_4);
        ((ImageButton) thumb4View).setImageDrawable(place.getPhotos().get(3));
        thumb4View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(thumb4View, place.getPhotos().get(3));
            }
        });

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
    }

    private void zoomImageFromThumb(final View thumbView, Drawable drawable) {
        ((ImageButton) thumbView).setImageDrawable(drawable);
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);
        expandedImageView.setImageDrawable(drawable);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.activity_place)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
}
