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
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.insulardevelopment.touristslittlehelper.ChosenPlaces;
import com.insulardevelopment.touristslittlehelper.DataBaseHelper;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.network.Http;
import com.insulardevelopment.touristslittlehelper.place.Place;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private List<Place> places;
    private GoogleMap map;
    private Route route;
    private TextView timeTv, distanceTv, cityTv;
    private EditText nameEt;
    private Button saveRouteBtn;
    private DataBaseHelper helper;

    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try{
                data = Http.read(url[0]);
            }catch(Exception e){}
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            drawPath(result);
        }
    }

    public static void start(Context context){
        Intent intent = new Intent(context, RouteActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        nameEt = (EditText) findViewById(R.id.new_route_name_text_view);
        helper = new DataBaseHelper(this);
        saveRouteBtn = (Button) findViewById(R.id.save_route_btn);
        timeTv = (TextView) findViewById(R.id.new_route_time_text_view);
        distanceTv = (TextView) findViewById(R.id.new_route_distance_text_view);
        cityTv = (TextView) findViewById(R.id.city_name_new_route_text_view);
        places = ChosenPlaces.getInstance().getPlaces();
        route = new Route();
        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(places.get(0).getLatLng().latitude, places.get(0).getLatLng().longitude, 1);
            route.setCity(addresses.get(0).getLocality());
            cityTv.setText(route.getCity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.route_map);
        mapFragment.getMapAsync(this);
        new DownloadTask().execute(getMapsApiDirectionsUrl());
        saveRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    route.setName(nameEt.getText().toString());
                    helper.getRouteDao().create(route);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.animateCamera( CameraUpdateFactory.newLatLngZoom(places.get(0).getLatLng(), 13.0f ) );
        for(Place place: places){
            if(place.getLatLng()!=null) {
                map.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName()));
            }
        }
    }

    private String getMapsApiDirectionsUrl() {
        String origin = "origin=" + places.get(0).getLatLng().latitude + "," + places.get(0).getLatLng().longitude;
        String waypoints = "waypoints=optimize:true|";
        int i = -1;
        for(Place place: places){
            i++;
            if (i == 0 || i == places.size() - 1) continue;
            waypoints += (place.getLatLng().latitude + "," + place.getLatLng().longitude + "|");
        }
        String destination = "destination=" + places.get(places.size() - 1).getLatLng().latitude + "," + places.get(places.size() - 1).getLatLng().longitude;
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

            Polyline line = map.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(12)
                    .color(R.color.transparent_yellow)
                    .geodesic(true)
            );

        } catch (JSONException e) {

        }
    }
}