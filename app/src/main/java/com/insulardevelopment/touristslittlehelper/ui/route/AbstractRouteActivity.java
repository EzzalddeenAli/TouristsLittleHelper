package com.insulardevelopment.touristslittlehelper.ui.route;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.model.Route;
import com.insulardevelopment.touristslittlehelper.ui.AboutPlaceView;
import com.insulardevelopment.touristslittlehelper.ui.AbstractActivity;
import com.insulardevelopment.touristslittlehelper.ui.choosePlace.PlacesMapViewModel;

import java.util.List;

/**
 * Created by Маргарита on 31.08.2017.
 */

public abstract class AbstractRouteActivity extends AbstractActivity implements OnMapReadyCallback {

    protected GoogleMap map;
    protected TextView timeTv, distanceTv, cityTv;
    protected AboutPlaceView placeRl;
    protected SupportMapFragment mapFragment;
    protected LatLng moveToLatLng;
    protected Route route;
    protected List<Place> places;

    protected PlacesMapViewModel placesMapViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placesMapViewModel = getViewModel(PlacesMapViewModel.class);

        placesMapViewModel.getPlaceMutableLiveData().observe(this, place -> {
            placeRl.setPlace(place);
        });


    }

    protected void initViews(){
        timeTv = findViewById(R.id.route_time_text_view);
        distanceTv = findViewById(R.id.route_distance_text_view);
        cityTv = findViewById(R.id.city_name_route_text_view);
        placeRl = findViewById(R.id.route_place_info_rl);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.route_map);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.animateCamera( CameraUpdateFactory.newLatLngZoom(moveToLatLng, 13.0f ) );
        for(Place place: places){
            if (!(place.getName().equals(Place.START_PLACE) || place.getName().equals(Place.FINISH_PLACE))) {
                if (place.getLongitude() != 0) {
                    placesMapViewModel.setMarkerAndPlace(map.addMarker(new MarkerOptions().position(new LatLng(place.getLatitude(), place.getLongitude())).title(place.getName())), place);
                }
            }
        }
        map.setOnMarkerClickListener(marker -> placesMapViewModel.onMarkerClick(marker));
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
