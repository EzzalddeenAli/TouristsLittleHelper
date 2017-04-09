package com.insulardevelopment.touristslittlehelper.choose_location;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.insulardevelopment.touristslittlehelper.choose_places.ChoosePlacesActivity;
import com.insulardevelopment.touristslittlehelper.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*
*   Активити для выбора города
*/

public class ChooseLocationActivity extends FragmentActivity implements OnMapReadyCallback,  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private GoogleMap map;
    private Button nextButton;
    private AutoCompleteTextView locationEditText;
    private AutoCompletePredictionAdapter adapter;
    private String location;
    private List<AutocompletePrediction> predictions;
    private AutocompletePrediction selected;
    private Place city;
    private LatLng selectedLatLng;

//    private List<AutocompletePrediction> getPredictions(String query) {
//
//        LatLngBounds latLngBounds = new LatLngBounds(new LatLng(-0,0), new LatLng(0,0));
//        PendingResult<AutocompletePredictionBuffer> result = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, query, latLngBounds, new AutocompleteFilter.Builder()
//                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
//                .build());
//        AutocompletePredictionBuffer autocompletePredictions = result.await(60, TimeUnit.SECONDS);
//        Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
//        ArrayList resultList = new ArrayList<>(autocompletePredictions.getCount());
//        while (iterator.hasNext()) {
//            AutocompletePrediction prediction = iterator.next();
//            resultList.add(prediction);
//        }
//        return resultList;
//    }

    private PendingResult<AutocompletePredictionBuffer> getPredictions(String query) {

        LatLngBounds latLngBounds = new LatLngBounds(new LatLng(-0,0), new LatLng(0,0));
        PendingResult<AutocompletePredictionBuffer> result = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, query, latLngBounds, new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build());
        return result;
    }

    public static void start(Context context){
        Intent intent = new Intent(context, ChooseLocationActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        predictions = new ArrayList<>();
        adapter = new AutoCompletePredictionAdapter(this, R.layout.autocomplete_prediction_layout);
        adapter.setNotifyOnChange(true);
        nextButton = (Button) findViewById(R.id.next_btn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoosePlacesActivity.start(ChooseLocationActivity.this, selectedLatLng);
            }
        });
        locationEditText = (AutoCompleteTextView) findViewById(R.id.choose_location_search_place_edit_text);
        locationEditText.setAdapter(adapter);
        locationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Observable.just(s)
                        .map(str -> getPredictions(str.toString()))
                        .map(res -> res.await(60, TimeUnit.SECONDS))
                        .flatMap(Observable::from)
                        .toList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(predictions -> {
                            adapter.clear();
                            adapter.addAll(predictions);
                            ChooseLocationActivity.this.predictions = predictions;
                            adapter.notifyDataSetChanged();
                        });

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        locationEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = predictions.get(position);
                Places.GeoDataApi.getPlaceById(mGoogleApiClient, selected.getPlaceId())
                        .setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (places.getStatus().isSuccess()) {
                                    city = places.get(0);
                                    LatLng loc = city.getLatLng();
                                    map.clear();
                                    map.addMarker(new MarkerOptions().position(loc));
                                    map.moveCamera(CameraUpdateFactory.newLatLng(loc));
                                    selectedLatLng = loc;
                                }
                                places.release();
                            }
                        });
            }
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
            mGoogleApiClient.connect();
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                try {
                    Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses.size() > 0) {
                        location = addresses.get(0).getLocality();
                        locationEditText.setText(location);
                        
                    }
                    selectedLatLng = latLng;
                    map.clear();
                    map.addMarker(new MarkerOptions().position(latLng));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = gcd.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                if (addresses.size() > 0)
                    locationEditText.setText(addresses.get(0).getAddressLine(1));
                    location = addresses.get(0).getAddressLine(1);
            }
            LatLng loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            map.addMarker(new MarkerOptions().position(loc));
            map.moveCamera(CameraUpdateFactory.newLatLng(loc));
            selectedLatLng = loc;
        } catch (SecurityException e) {
        }catch (Exception e) {
            String s = e.getMessage();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
