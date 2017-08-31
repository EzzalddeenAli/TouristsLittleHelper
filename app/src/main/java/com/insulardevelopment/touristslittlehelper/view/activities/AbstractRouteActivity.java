package com.insulardevelopment.touristslittlehelper.view.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.model.Route;
import com.insulardevelopment.touristslittlehelper.view.AboutPlaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Маргарита on 31.08.2017.
 */

public abstract class AbstractRouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    protected GoogleMap map;
    protected TextView timeTv, distanceTv, cityTv;
    protected AboutPlaceView placeRl;
    protected SupportMapFragment mapFragment;
    protected LatLng moveToLatLng;
    protected Route route;
    protected List<Place> places;
    protected List<Marker> markers;

    protected void initViews(){
        timeTv = (TextView) findViewById(R.id.route_time_text_view);
        distanceTv = (TextView) findViewById(R.id.route_distance_text_view);
        cityTv = (TextView) findViewById(R.id.city_name_route_text_view);
        placeRl = (AboutPlaceView) findViewById(R.id.route_place_info_rl);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.route_map);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.animateCamera( CameraUpdateFactory.newLatLngZoom(moveToLatLng, 13.0f ) );
        for(Place place: places){
            if (!(place.getName().equals(Place.START_PLACE) || place.getName().equals(Place.FINISH_PLACE))) {
                if (place.getLongitude() != 0) {
                    markers.add(map.addMarker(new MarkerOptions().position(new LatLng(place.getLatitude(), place.getLongitude())).title(place.getName())));
                }
            }
        }
        map.setOnMarkerClickListener(marker -> {
            Place place;
            if (route.isHasStartAndFinish()){
                place = places.get(markers.indexOf(marker) + 1);
            } else {
                place = places.get(markers.indexOf(marker));
            }
            placeRl.setPlace(place);
            return true;
        });
    }

    protected void drawRoute(List<LatLng> points){
        map.addPolyline(new PolylineOptions()
                .addAll(points)
                .width(12)
                .color(ContextCompat.getColor(this, R.color.maps_color))
                .geodesic(true)
        );
    }

    protected void setContent(){
        cityTv.setText(route.getCity());
        timeTv.setText(String.valueOf(String.format("%.1f", ((double) route.getTime())/3600)) + "ч");
        distanceTv.setText(String.valueOf(String.format("%.1f", ((double) route.getDistance())/1000)) + "км");
    }
}
