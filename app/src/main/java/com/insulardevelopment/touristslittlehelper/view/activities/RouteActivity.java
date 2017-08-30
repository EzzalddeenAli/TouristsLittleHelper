package com.insulardevelopment.touristslittlehelper.view.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
 * Активити, для показа информации о маршруте
 */

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String CHOSEN_ROUTE = "chosen route";

    private GoogleMap map;
    private TextView nameTv, timeTv, distanceTv, cityTv;
    private AboutPlaceView placeRl;
    private SupportMapFragment mapFragment;
    private List<LatLng> points;
    private Route route;
    private List<Marker> markers;

    public static void start(Context context, Route route){
        Intent intent = new Intent(context, RouteActivity.class);
        intent.putExtra(CHOSEN_ROUTE, route);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        initViews();
        route = (Route) getIntent().getSerializableExtra(CHOSEN_ROUTE);
        mapFragment.getMapAsync(this);
        markers = new ArrayList<>();
        points = Route.decodePoly(route.getEncodedPoly());
        setContent();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.animateCamera( CameraUpdateFactory.newLatLngZoom(points.get(0), 13.0f ) );
        drawRoute();
        for(Place place: route.getPlaces()){
            if (!(place.getName().equals(Place.START_PLACE) || place.getName().equals(Place.FINISH_PLACE))) {
                if (place.getLongitude() != 0) {
                    markers.add(map.addMarker(new MarkerOptions().position(new LatLng(place.getLatitude(), place.getLongitude())).title(place.getName())));
                }
            }
        }
        map.setOnMarkerClickListener(marker -> {
            Place place;
            if (route.isHasStartAndFinish()){
                place = route.getPlaces().get(markers.indexOf(marker) + 1);
            } else {
                place = route.getPlaces().get(markers.indexOf(marker));
            }
            placeRl.setPlace(place);
            return true;
        });
    }

    private void drawRoute(){
        map.addPolyline(new PolylineOptions()
                .addAll(points)
                .width(12)
                .color(ContextCompat.getColor(this, R.color.maps_color))
                .geodesic(true)
        );
    }

    private void initViews(){
        nameTv = (TextView) findViewById(R.id.route_activity_name_text_view);
        timeTv = (TextView) findViewById(R.id.route_activity_time_text_view);
        distanceTv = (TextView) findViewById(R.id.route_activity_distance_text_view);
        cityTv = (TextView) findViewById(R.id.city_name_route_activity_text_view);
        placeRl = (AboutPlaceView) findViewById(R.id.route_place_info_rl);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.route_activity_map);
    }

    private void setContent(){
        nameTv.setText(route.getName());
        cityTv.setText(route.getCity());
        timeTv.setText(String.valueOf(String.format("%.1f", ((double) route.getTime())/3600)) + "ч");
        distanceTv.setText(String.valueOf(String.format("%.1f", ((double) route.getDistance())/1000)) + "км");
    }
}
