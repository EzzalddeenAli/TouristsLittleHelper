package com.insulardevelopment.touristslittlehelper.route;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.insulardevelopment.touristslittlehelper.MainActivity;
import com.insulardevelopment.touristslittlehelper.database.DataBaseHelper;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.network.Http;
import com.insulardevelopment.touristslittlehelper.network.Newtork;
import com.insulardevelopment.touristslittlehelper.place.Place;
import com.insulardevelopment.touristslittlehelper.place.PlaceActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewRouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String PLACES = "places";

    private List<Place> places;
    private GoogleMap map;
    private Route route;
    private TextView timeTv, distanceTv, cityTv, placeNameTv, addressTv;
    private Button moreInfoBtn;
    private ImageView placeIconIv;
    private ImageButton closeIv;
    private EditText nameEt;
    private Button saveRouteBtn;
    private DataBaseHelper helper;
    private RelativeLayout placeRl;
    private List<Marker> markers;


    public static void start(Context context, ArrayList<Place> places){
        Intent intent = new Intent(context, NewRouteActivity.class);
        intent.putExtra(PLACES, places);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route);
        nameEt = (EditText) findViewById(R.id.new_route_name_text_view);
        helper = new DataBaseHelper(this);
        saveRouteBtn = (Button) findViewById(R.id.save_route_btn);
        timeTv = (TextView) findViewById(R.id.new_route_time_text_view);
        distanceTv = (TextView) findViewById(R.id.new_route_distance_text_view);
        cityTv = (TextView) findViewById(R.id.city_name_new_route_text_view);
        placeNameTv = (TextView) findViewById(R.id.new_route_place_name_info_text_view);
        addressTv = (TextView) findViewById(R.id.new_route_place_address_text_view);
        moreInfoBtn = (Button) findViewById(R.id.new_route_more_info_place_btn);
        placeIconIv = (ImageView) findViewById(R.id.new_route_place_info_icon_iv);
        closeIv = (ImageButton) findViewById(R.id.new_route_close_ib);
        placeRl = (RelativeLayout) findViewById(R.id.new_route_place_info_rl);
        places = (List<Place>) getIntent().getSerializableExtra(PLACES);
        route = new Route();
        markers = new ArrayList<>();
        changePlacesOrder(places);
        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(places.get(0).getLatitude(), places.get(0).getLongitude(), 1);
            route.setCity(addresses.get(0).getLocality());
            cityTv.setText(route.getCity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.route_map);
        mapFragment.getMapAsync(this);

        Observable.just(getMapsApiDirectionsUrl())
                .map(s -> {
                    try {
                        return Http.read(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "";
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                    drawPath(s);
                });

        saveRouteBtn.setOnClickListener(view -> {
            try {
                route.setName(nameEt.getText().toString());
                helper.getRouteDao().create(route);
                for(Place place: places){
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
        map.animateCamera( CameraUpdateFactory.newLatLngZoom(latLng, 13.0f ) );
        for(Place place: places){
            if(place.getLongitude()!=0) {
                markers.add(map.addMarker(new MarkerOptions().position( new LatLng(place.getLatitude(), place.getLongitude())).title(place.getName())));
            }
        }
    }

    private String getMapsApiDirectionsUrl() {
        String origin = "origin=" + places.get(0).getLatitude() + "," + places.get(0).getLongitude();
        String waypoints = "waypoints=optimize:true|";
        int i = -1;
        for(Place place: places){
            i++;
            if (i == 0 || i == places.size() - 1) continue;
            waypoints += (place.getLatitude() + "," + place.getLongitude() + "|");
        }
        String destination = "destination=" + places.get(places.size() - 1).getLatitude() + "," + places.get(places.size() - 1).getLongitude();
        String sensor = "sensor=false";
        String mode = "mode=walking";
        String params = origin + "&" + waypoints + "&" + destination + "&" + mode + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params;
        return url;
    }



    public void drawPath(String result) {

        try {
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            route.setEncodedPoly(encodedString);
            int distance = 0, time = 0;
            JSONArray legs = routes.getJSONArray("legs");
            int count = legs.length();
            for (int i = 0; i < count; i++){
                JSONObject jsonObject = (JSONObject)legs.get(i);
                distance += ((JSONObject)jsonObject.get("distance")).getInt("value");
                time += ((JSONObject)jsonObject.get("duration")).getInt("value");
            }
            timeTv.setText(String.valueOf(String.format("%.1f", ((double) time)/3600)) + "ч");
            distanceTv.setText(String.valueOf(String.format("%.1f", ((double) distance)/1000)) + "км");

            route.setTime(time);
            route.setDistance(distance);

            List<LatLng> list = Route.decodePoly(encodedString);

            map.addPolyline(new PolylineOptions()
                    .addAll(list)
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
                    addressTv.setText(place.getFormattedAddress());
                    Glide.with(NewRouteActivity.this)
                            .load(place.getIcon())
                            .into(placeIconIv);
                    moreInfoBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PlaceActivity.start(NewRouteActivity.this, place);
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

        } catch (JSONException e) {

        }
    }

    private void changePlacesOrder(List<Place> places){
        final int R = 6371;
        double max = 0;
        Place startPlace = null, finishPlace = null;
        for (Place place1: places){
            for (Place place2: places){
                if(place1 != place2){
                    double latDistance = Math.toRadians(place2.getLatitude() - place1.getLatitude());
                    double lonDistance = Math.toRadians(place2.getLongitude() - place1.getLongitude());
                    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                            + Math.cos(Math.toRadians(place1.getLatitude())) * Math.cos(Math.toRadians(place2.getLatitude()))
                            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                    double distance = R * c * 1000;
                    if(distance > max) {
                        max = distance;
                        startPlace = place1;
                        finishPlace = place2;
                    }
                }
            }
        }
        places.remove(startPlace);
        places.remove(finishPlace);
        places.add(0, startPlace);
        places.add(finishPlace);
    }
}