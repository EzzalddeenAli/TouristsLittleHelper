package com.insulardevelopment.touristslittlehelper.route;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.place.Place;
import com.insulardevelopment.touristslittlehelper.place.PlaceActivity;

import java.util.ArrayList;
import java.util.List;

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private List<LatLng> points;
    private Route route;
    private TextView nameTv, timeTv, distanceTv, cityTv, placeNameTv;
    private Button moreInfoBtn;
    private ImageView placeIconIv;
    private ImageButton closeIv;
    private List<Marker> markers;
    private RelativeLayout placeRl;


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
        markers = new ArrayList<>();
        route = (Route) getIntent().getSerializableExtra(CHOSEN_ROUTE);
        points = Route.decodePoly(route.getEncodedPoly());

        nameTv = (TextView) findViewById(R.id.route_activity_name_text_view);
        timeTv = (TextView) findViewById(R.id.route_activity_time_text_view);
        distanceTv = (TextView) findViewById(R.id.route_activity_distance_text_view);
        cityTv = (TextView) findViewById(R.id.city_name_route_activity_text_view);
        placeNameTv = (TextView) findViewById(R.id.route_place_name_info_text_view);
        moreInfoBtn = (Button) findViewById(R.id.route_more_info_place_btn);
        placeIconIv = (ImageView) findViewById(R.id.route_place_info_icon_iv);
        closeIv = (ImageButton) findViewById(R.id.route_close_ib);
        placeRl = (RelativeLayout) findViewById(R.id.route_place_info_rl);
        nameTv.setText(route.getName());
        cityTv.setText(route.getCity());
        timeTv.setText(String.valueOf(String.format("%.1f", ((double) route.getTime())/3600)) + "ч");
        distanceTv.setText(String.valueOf(String.format("%.1f", ((double) route.getDistance())/1000)) + "км");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.animateCamera( CameraUpdateFactory.newLatLngZoom(points.get(0), 13.0f ) );
        final List<Place> places = route.getPlaces();
        for(Place place: route.getPlaces()){
            markers.add(map.addMarker(new MarkerOptions().position(new LatLng(place.getLatitude(), place.getLongitude()))));
        }
        map.addPolyline(new PolylineOptions()
                .addAll(points)
                .width(12)
                .color(R.color.transparent_yellow)
                .geodesic(true)
        );
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int i = markers.indexOf(marker);
                final Place place = places.get(markers.indexOf(marker));
                placeRl.setVisibility(View.VISIBLE);
                placeNameTv.setText(place.getName());
                Glide.with(RouteActivity.this)
                        .load(place.getIcon())
                        .into(placeIconIv);
                moreInfoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PlaceActivity.start(RouteActivity.this, place);
                    }
                });
                closeIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        placeRl.setVisibility(View.INVISIBLE);
                    }
                });
                return true;
            }
        });
    }
}
