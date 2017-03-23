package com.insulardevelopment.touristslittlehelper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.events.ChangeEvent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.insulardevelopment.touristslittlehelper.place.Http;
import com.insulardevelopment.touristslittlehelper.place.Place;
import com.insulardevelopment.touristslittlehelper.route.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private List<Place> places;
    private GoogleMap map;
    private Route route;
    private TextView timeTv, distanceTv;

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
        timeTv = (TextView) findViewById(R.id.new_route_time_text_view);
        distanceTv = (TextView) findViewById(R.id.new_route_distance_text_view);
        places = ChosenPlaces.getInstance().getPlaces();
        route = new Route();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.route_map);
        mapFragment.getMapAsync(this);
        new DownloadTask().execute(getMapsApiDirectionsUrl());
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