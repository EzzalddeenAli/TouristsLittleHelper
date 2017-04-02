package com.insulardevelopment.touristslittlehelper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.insulardevelopment.touristslittlehelper.place.Place;
import com.insulardevelopment.touristslittlehelper.route.NewRouteActivity;
import com.insulardevelopment.touristslittlehelper.route.Route;

import java.util.List;

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private List<LatLng> places;
    private Route route;
    private TextView nameTv, timeTv, distanceTv, cityTv;

    private static final String CHOSEN_ROUTE = "chosen route";

    public static void start(Context context, Route route){
        Intent intent = new Intent(context, RouteActivity.class);
        intent.putExtra(CHOSEN_ROUTE, route);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.route_activity_map);
        mapFragment.getMapAsync(this);
        route = (Route) getIntent().getSerializableExtra(CHOSEN_ROUTE);
        places = Route.decodePoly(route.getEncodedPoly());

        nameTv = (TextView) findViewById(R.id.route_activity_name_text_view);
        timeTv = (TextView) findViewById(R.id.route_activity_time_text_view);
        distanceTv = (TextView) findViewById(R.id.route_activity_distance_text_view);
        cityTv = (TextView) findViewById(R.id.city_name_route_activity_text_view);
        nameTv.setText(route.getName());
        cityTv.setText(route.getCity());
        timeTv.setText(String.valueOf(String.format("%.1f", ((double) route.getTime())/3600)) + "ч");
        distanceTv.setText(String.valueOf(String.format("%.1f", ((double) route.getDistance())/1000)) + "км");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.animateCamera( CameraUpdateFactory.newLatLngZoom(places.get(0), 13.0f ) );
//        for(LatLng place: places){
//            map.addMarker(new MarkerOptions().position(place));
//        }
        map.addPolyline(new PolylineOptions()
                .addAll(places)
                .width(12)
                .color(R.color.transparent_yellow)
                .geodesic(true)
        );
    }
}
