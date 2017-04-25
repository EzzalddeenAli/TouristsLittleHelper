package com.insulardevelopment.touristslittlehelper.view.activities;

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
import android.widget.Toast;

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
import com.insulardevelopment.touristslittlehelper.model.Route;
import com.insulardevelopment.touristslittlehelper.network.Newtork;
import com.insulardevelopment.touristslittlehelper.model.Place;

import java.util.ArrayList;
import java.util.List;
/**
 * Активити, для показа информации о маршруте
 */

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String CHOSEN_ROUTE = "chosen route";

    private GoogleMap map;
    private TextView nameTv, timeTv, distanceTv, cityTv, placeNameTv, addressTv;
    private Button moreInfoBtn;
    private ImageView placeIconIv;
    private ImageButton closeIv;
    private RelativeLayout placeRl;
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
            Place place = route.getPlaces().get(markers.indexOf(marker));
            setInfoLayoutContent(place);
            return true;
        });
    }

    private void setInfoLayoutContent(Place place){
        placeRl.setVisibility(View.VISIBLE);
        placeNameTv.setText(place.getName());
        addressTv.setText(place.getFormattedAddress());
        Glide.with(RouteActivity.this)
                .load(place.getIcon())
                .into(placeIconIv);
        moreInfoBtn.setOnClickListener(view -> {
            if (Newtork.isAvalaible(RouteActivity.this)) {
                PlaceActivity.start(RouteActivity.this, place);
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_connection), Toast.LENGTH_LONG).show();
            }
        });
        closeIv.setOnClickListener(view -> placeRl.setVisibility(View.INVISIBLE));
    }

    private void drawRoute(){
        map.addPolyline(new PolylineOptions()
                .addAll(points)
                .width(12)
                .color(R.color.transparent_yellow)
                .geodesic(true)
        );
    }

    private void initViews(){
        nameTv = (TextView) findViewById(R.id.route_activity_name_text_view);
        timeTv = (TextView) findViewById(R.id.route_activity_time_text_view);
        distanceTv = (TextView) findViewById(R.id.route_activity_distance_text_view);
        cityTv = (TextView) findViewById(R.id.city_name_route_activity_text_view);
        placeNameTv = (TextView) findViewById(R.id.route_place_name_info_text_view);
        addressTv = (TextView) findViewById(R.id.route_place_address_text_view);
        moreInfoBtn = (Button) findViewById(R.id.route_more_info_place_btn);
        placeIconIv = (ImageView) findViewById(R.id.route_place_info_icon_iv);
        closeIv = (ImageButton) findViewById(R.id.route_close_ib);
        placeRl = (RelativeLayout) findViewById(R.id.route_place_info_rl);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.route_activity_map);
    }

    private void setContent(){
        nameTv.setText(route.getName());
        cityTv.setText(route.getCity());
        timeTv.setText(String.valueOf(String.format("%.1f", ((double) route.getTime())/3600)) + "ч");
        distanceTv.setText(String.valueOf(String.format("%.1f", ((double) route.getDistance())/1000)) + "км");
    }
}
