package com.insulardevelopment.touristslittlehelper.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.insulardevelopment.touristslittlehelper.view.adapters.AutoCompletePredictionAdapter;
import com.insulardevelopment.touristslittlehelper.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*
*   Активити для выбора города
*/

public class ChooseLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks {

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private List<AutocompletePrediction> predictions;

    private Button nextButton;
    private AutoCompleteTextView locationEditText;
    private AutoCompletePredictionAdapter adapter;

    private String location;
    private Place city;
    private LatLng selectedLatLng;

    public static void start(Context context) {
        Intent intent = new Intent(context, ChooseLocationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initViews();
        setupLocationEditText();
        setupGoogleClient();
        predictions = new ArrayList<>();
        nextButton.setOnClickListener(v -> {
            if (selectedLatLng != null) {
                ChoosePlacesActivity.start(ChooseLocationActivity.this, selectedLatLng);
            } else {
                Toast.makeText(ChooseLocationActivity.this, getString(R.string.no_city_chosen), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(latLng -> {
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
        });
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = gcd.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1);
                if (addresses.size() > 0) {
                    location = addresses.get(0).getAddressLine(1);
                    locationEditText.setText(location);
                }
            }
            if (lastLocation != null) {
                selectedLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                map.addMarker(new MarkerOptions().position(selectedLatLng));
                map.moveCamera(CameraUpdateFactory.newLatLng(selectedLatLng));
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private PendingResult<AutocompletePredictionBuffer> getPredictions(String query) {
        LatLngBounds latLngBounds = new LatLngBounds(new LatLng(-0, 0), new LatLng(0, 0));
        return Places.GeoDataApi.getAutocompletePredictions(googleApiClient, query, latLngBounds, new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build());
    }

    private void setupGoogleClient(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
            googleApiClient.connect();
        }
    }

    private void setupLocationEditText(){
        locationEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                nextButton.performClick();
                return true;
            }
            return false;
        });

        adapter = new AutoCompletePredictionAdapter(this, R.layout.autocomplete_prediction_layout);
        adapter.setNotifyOnChange(true);
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
                          .flatMapIterable(predictions -> predictions)
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

        locationEditText.setOnItemClickListener((parent, view, position, id) -> {
            AutocompletePrediction selected = predictions.get(position);
            Places.GeoDataApi.getPlaceById(googleApiClient, selected.getPlaceId())
                             .setResultCallback(places -> {
                                 if (places.getStatus().isSuccess()) {
                                     city = places.get(0);
                                     selectedLatLng = city.getLatLng();
                                     map.clear();
                                     map.addMarker(new MarkerOptions().position(selectedLatLng));
                                     map.moveCamera(CameraUpdateFactory.newLatLng(selectedLatLng));
                                 }
                                 places.release();
                             });
        });
    }

    private void initViews(){
        nextButton = findViewById(R.id.next_btn);
        locationEditText = findViewById(R.id.choose_location_search_place_edit_text);
    }

}
