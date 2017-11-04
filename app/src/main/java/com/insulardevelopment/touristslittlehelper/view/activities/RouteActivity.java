package com.insulardevelopment.touristslittlehelper.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Route;

import java.util.ArrayList;

/**
 * Активити, для показа информации о маршруте
 */

public class RouteActivity extends AbstractRouteActivity {

    private static final String CHOSEN_ROUTE = "chosen route";

    private TextView nameTv;

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
        places = route.getPlaces();
        moveToLatLng = new LatLng(route.getPlaces().get(0).getLatitude(), route.getPlaces().get(0).getLongitude());
        setContent();
        mapFragment.getMapAsync(this);
        markers = new ArrayList<>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        drawRoute(route.decodePoly());
    }

    protected void initViews(){
        nameTv = findViewById(R.id.route_activity_name_text_view);
        super.initViews();
    }

    protected void setContent(){
        nameTv.setText(route.getName());
        super.setContent();
    }
}
