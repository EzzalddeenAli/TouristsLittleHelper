package com.insulardevelopment.touristslittlehelper;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.insulardevelopment.touristslittlehelper.place.Http;
import com.insulardevelopment.touristslittlehelper.place.Place;
import com.insulardevelopment.touristslittlehelper.place.PlaceActivity;
import com.insulardevelopment.touristslittlehelper.place.PlaceAdapter;
import com.insulardevelopment.touristslittlehelper.place.PlaceParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Маргарита on 02.02.2017.
 */

public class PlacesListFragment extends Fragment  implements LocationListener {

    private View view;
    private GoogleApiClient googleApiClient;
    private ArrayList<Place> places = new ArrayList<>();
    private PlaceAdapter placeAdapter;
    private Button nextButton;
    private String location;
    private double lat =  51.67;
    private double lng = 39.21;
    private int radius = 5000;


    public class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {
        String googlePlacesData = null;
        JSONObject googlePlacesJson;

        @Override
        protected String doInBackground(Object... inputObj) {
            try {
                String googlePlacesUrl = (String) inputObj[0];
                Http http = new Http();
                googlePlacesData = http.read(googlePlacesUrl);
            } catch (Exception e) {
            }
            return googlePlacesData;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                googlePlacesJson = new JSONObject(result);
                List<Place> googlePlacesList = (new PlaceParser()).parse(googlePlacesJson);
                placeAdapter = new PlaceAdapter(googlePlacesList, new PlaceAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        PlaceActivity.start(getActivity());
                    }
                });
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                RecyclerView popularPlacesRecyclerView = (RecyclerView) view.findViewById(R.id.popular_places_recycler_view);

                popularPlacesRecyclerView.setLayoutManager(layoutManager);
                popularPlacesRecyclerView.setAdapter(placeAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static PlacesListFragment newInstance(){
        return new PlacesListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_places_list, container, false);
        setRetainInstance(true);
        Intent intent = getActivity().getIntent();
        location = intent.getStringExtra("location");

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);

        try{
            locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

            String type = "museum|art_gallery";
            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            googlePlacesUrl.append("location=" + lat + "," + lng);
            googlePlacesUrl.append("&radius=" + radius);
            googlePlacesUrl.append("&types=" + type);
            googlePlacesUrl.append("&sensor=true");
            googlePlacesUrl.append("&key=" + "AIzaSyCjnoH7MNT5iS90ZHk4cV_fYj3ZZTKKp_Y");

            PlacesListFragment.GooglePlacesReadTask googlePlacesReadTask = new PlacesListFragment.GooglePlacesReadTask();
            Object toPass = googlePlacesUrl.toString();
            googlePlacesReadTask.execute(toPass);
        }catch (SecurityException e){}
        nextButton = (Button) view.findViewById(R.id.next_btn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    TestActivity.start(PopularPlacesActivity.this);
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
