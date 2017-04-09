package com.insulardevelopment.touristslittlehelper.choose_places;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.database.DataBaseHelper;
import com.insulardevelopment.touristslittlehelper.network.Http;
import com.insulardevelopment.touristslittlehelper.place.Place;
import com.insulardevelopment.touristslittlehelper.place.PlaceParser;
import com.insulardevelopment.touristslittlehelper.placetype.PlaceType;
import com.insulardevelopment.touristslittlehelper.route.NewRouteActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChoosePlacesActivity extends AppCompatActivity {

    private static final String SELECTED_LATLNG = "latlng";
    private LatLng selectedLatLng;
    private int radius = 5000;
    private List<Place> places;
    private Button nextBtn;


    public static void start(Context context, LatLng latLng){
        Intent intent = new Intent(context, ChoosePlacesActivity.class);
        intent.putExtra(SELECTED_LATLNG, latLng);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_places);
        Intent intent = getIntent();
        setTitle(R.string.popular_places_activity_description);
        selectedLatLng = intent.getParcelableExtra(SELECTED_LATLNG);
        String strTypes = "";
        DataBaseHelper helper = new DataBaseHelper(this);
        try {
            List<PlaceType> types = helper.getTypeDao().queryForAll();
            for (PlaceType type : types) {
                strTypes += (type.getTypesName() + "|");
            }
            strTypes = strTypes.substring(0, strTypes.length() - 1);
        }
        catch (Exception e) {}

        final ViewPager placesViewPager = (ViewPager) findViewById(R.id.places_view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + selectedLatLng.latitude + "," + selectedLatLng.longitude);
        googlePlacesUrl.append("&radius=" + radius);
        googlePlacesUrl.append("&types=" + strTypes);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&language=ru");
        googlePlacesUrl.append("&key=" + "AIzaSyCjnoH7MNT5iS90ZHk4cV_fYj3ZZTKKp_Y");
        String query = googlePlacesUrl.toString();

        Observable.just(query)
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
                        return PlaceParser.getSomeInfo(new JSONObject(s));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(places -> {
                    PlacesPagerAdapter adapter = new PlacesPagerAdapter(fragmentManager, selectedLatLng, places);
                    placesViewPager.setAdapter(adapter);
                    ChoosePlacesActivity.this.places = places;
                });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(placesViewPager);
        nextBtn = (Button) findViewById(R.id.to_route_btn);
        nextBtn.setOnClickListener(view -> {
            ArrayList<Place> chosenPlaces = new ArrayList<>();
            for(Place p: places){
                if (p.isChosen()) chosenPlaces.add(p);
            }
            NewRouteActivity.start(ChoosePlacesActivity.this, chosenPlaces);
        });
    }

}
