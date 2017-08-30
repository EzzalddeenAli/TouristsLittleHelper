package com.insulardevelopment.touristslittlehelper.view.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.insulardevelopment.touristslittlehelper.database.DataBaseHelper;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.model.Route;
import com.insulardevelopment.touristslittlehelper.network.APIWorker;
import com.insulardevelopment.touristslittlehelper.view.AboutPlaceView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Активити для построения маршрута
 */

public class NewRouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String START_AND_FINISH = "start and finish";

    private List<Place> places;
    private GoogleMap map;
    private Route route;
    private TextView timeTv, distanceTv, cityTv;
    private EditText nameEt;
    private Button saveRouteBtn;
    private DataBaseHelper helper;
    private AboutPlaceView aboutPlaceView;
    private List<Marker> markers;
    private boolean hasStartAndFinish;


    public static void start(Context context, boolean hasStartAndFinish) {
        Intent intent = new Intent(context, NewRouteActivity.class);
        intent.putExtra(START_AND_FINISH, hasStartAndFinish);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initViews();
        helper = new DataBaseHelper(this);
        places = ChoosePlacesActivity.getChosenPlaces();
        hasStartAndFinish = getIntent().getBooleanExtra(START_AND_FINISH, false);
        route = new Route();
        markers = new ArrayList<>();
        changePlacesOrder(places, hasStartAndFinish);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.route_map);
        mapFragment.getMapAsync(this);

        APIWorker.getRoute(places, getResources().getString(R.string.google_api_key))
                .subscribe(this::setupRoute);

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(places.get(0).getLatitude(), places.get(0).getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0f));
        for (Place place : places) {
            if (!(place.getName().equals(Place.START_PLACE) || place.getName().equals(Place.FINISH_PLACE))) {
                if (place.getLongitude() != 0) {
                    markers.add(map.addMarker(new MarkerOptions()
                            .position(new LatLng(place.getLatitude(), place.getLongitude()))
                            .title(place.getName())));
                }
            }
        }
    }

    private void setupRoute(Route serverRoute) {
        route = serverRoute;
        timeTv.setText(String.valueOf(String.format("%.1f", ((double) serverRoute.getTime()) / 3600)) + "ч");
        distanceTv.setText(String.valueOf(String.format("%.1f", ((double) serverRoute.getDistance()) / 1000)) + "км");

        drawPath(Route.decodePoly(serverRoute.getEncodedPoly()));
        route.setCity(getCity());
        cityTv.setText(route.getCity());

        serverRoute.setHasStartAndFinish(hasStartAndFinish);
        map.setOnMarkerClickListener(marker -> {
            Place place;
            if (hasStartAndFinish) {
                place = places.get(markers.indexOf(marker) + 1);
            } else {
                place = places.get(markers.indexOf(marker));
            }
            aboutPlaceView.setPlace(place);
            return true;
        });

    }

    private void drawPath(List<LatLng> points){
        map.addPolyline(new PolylineOptions()
                .addAll(points)
                .width(12)
                .color(ContextCompat.getColor(this, R.color.maps_color))
                .geodesic(true)
        );
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

    private void initViews() {
        nameEt = (EditText) findViewById(R.id.new_route_name_text_view);
        saveRouteBtn = (Button) findViewById(R.id.save_route_btn);
        timeTv = (TextView) findViewById(R.id.new_route_time_text_view);
        distanceTv = (TextView) findViewById(R.id.new_route_distance_text_view);
        cityTv = (TextView) findViewById(R.id.city_name_new_route_text_view);
        aboutPlaceView = (AboutPlaceView) findViewById(R.id.new_route_place_info_rl);
    }
}