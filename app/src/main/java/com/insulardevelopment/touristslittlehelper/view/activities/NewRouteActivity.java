package com.insulardevelopment.touristslittlehelper.view.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.database.DataBaseHelper;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.model.Route;
import com.insulardevelopment.touristslittlehelper.view.viewmodel.RouteViewModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Активити для построения маршрута
 */

public class NewRouteActivity extends AbstractRouteActivity{

    private static final String START_AND_FINISH = "start and finish";

    private EditText nameEt;
    private Button saveRouteBtn;
    private DataBaseHelper helper;
    private boolean hasStartAndFinish;

    private RouteViewModel routeViewModel;

    public static void start(Context context, boolean hasStartAndFinish) {
        Intent intent = new Intent(context, NewRouteActivity.class);
        intent.putExtra(START_AND_FINISH, hasStartAndFinish);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route);

        routeViewModel = getViewModel(RouteViewModel.class);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initViews();
        helper = new DataBaseHelper(this);
        places = ChoosePlacesActivity.getChosenPlaces();
        hasStartAndFinish = getIntent().getBooleanExtra(START_AND_FINISH, false);
        markers = new ArrayList<>();
        moveToLatLng = new LatLng(places.get(0).getLatitude(), places.get(0).getLongitude());
        mapFragment.getMapAsync(this);
        changePlacesOrder(places, hasStartAndFinish);

        routeViewModel.getRoute(places, getResources().getString(R.string.google_api_key))
                .observe(this, this::setupRoute);

        saveRouteBtn.setOnClickListener(view -> {
            try {
                route.setHasStartAndFinish(hasStartAndFinish);
                route.setName(nameEt.getText().toString());
                helper.getRouteDao().create(route);
                for (Place place : places) {
                    place.setRoute(route);
                    helper.getPlaceDao().create(place);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            MainActivity.start(NewRouteActivity.this);
        });
    }

    private void setupRoute(Route serverRoute) {
        route = serverRoute;
        route.setCity(getCity());
        setContent();
        drawRoute(serverRoute.decodePoly());
    }

    private String getCity(){
        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(places.get(0).getLatitude(), places.get(0).getLongitude(), 1);
            return addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void changePlacesOrder(List<Place> places, boolean hasStartAndFinish) {
        Place startPlace = null, finishPlace = null;
        if (hasStartAndFinish) {
            for (Place place : places) {
                if (place.getName().equals(Place.START_PLACE)) {
                    startPlace = place;
                }
                if (place.getName().equals(Place.FINISH_PLACE)) {
                    finishPlace = place;
                }
            }
            if (finishPlace == null) {
                finishPlace = findMostFarPlace(startPlace);
            }
            if (startPlace == null) {
                startPlace = findMostFarPlace(finishPlace);
            }
        } else {
            double max = 0;
            for (Place place1 : places) {
                for (Place place2 : places) {
                    if (place1 != place2) {
                        double distance = findDistance(place1, place2);
                        if (distance > max) {
                            max = distance;
                            startPlace = place1;
                            finishPlace = place2;
                        }
                    }
                }
            }
        }
        places.remove(startPlace);
        places.remove(finishPlace);
        places.add(0, startPlace);
        places.add(finishPlace);
    }

    private Place findMostFarPlace(Place place1) {
        Place place2 = null;
        double max = 0;
        for (Place place : places) {
            if (place1 != place) {
                double distance = findDistance(place1, place);
                if (distance > max) {
                    max = distance;
                    place2 = place;
                }
            }
        }
        return place2;
    }

    private double findDistance(Place place1, Place place2) {
        final int earthRadius = 6371;
        double latDistance = Math.toRadians(place2.getLatitude() - place1.getLatitude());
        double lonDistance = Math.toRadians(place2.getLongitude() - place1.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(place1.getLatitude())) * Math.cos(Math.toRadians(place2.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c * 1000;
    }

    protected void initViews() {
        nameEt = findViewById(R.id.route_name_text_view);
        saveRouteBtn = findViewById(R.id.save_route_btn);
        super.initViews();
    }
}