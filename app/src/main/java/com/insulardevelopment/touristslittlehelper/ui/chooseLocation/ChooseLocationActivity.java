package com.insulardevelopment.touristslittlehelper.ui.chooseLocation;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.ui.AbstractActivity;
import com.insulardevelopment.touristslittlehelper.ui.choosePlace.ChoosePlacesActivity;

import java.io.IOException;
import java.util.Locale;
/*
*   Активити для выбора города
*/

public class ChooseLocationActivity extends AbstractActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks {

    private GoogleMap map;
    private GoogleApiClient googleApiClient;

    private Button nextButton;
    private AutoCompleteTextView locationEditText;
    private AutoCompletePredictionAdapter adapter;

    private LocationViewModel locationViewModel;

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

        locationViewModel = getViewModel(LocationViewModel.class);

        initViews();
        setupLocationEditText();
        setupGoogleClient();
        nextButton.setOnClickListener(v -> {
            if (locationViewModel.getSelectedLatLng() != null) {
                ChoosePlacesActivity.start(ChooseLocationActivity.this, locationViewModel.getSelectedLatLng());
            } else {
                Toast.makeText(ChooseLocationActivity.this, getString(R.string.no_city_chosen), Toast.LENGTH_SHORT).show();
            }
        });

        locationViewModel.getLatLngLiveData()
                .observe(this, latLng -> {
                    map.clear();
                    map.addMarker(new MarkerOptions().position(latLng));
                    map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                });

        locationViewModel.getPredictionsLiveData()
                .observe(this, autocompletePredictions -> {
                    adapter.setData(autocompletePredictions);
                });

        locationViewModel.getPlaceLiveData()
                .observe(this, s -> {
                    locationEditText.setText(s);
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(latLng -> {
            try {
                locationViewModel.setLatLng(latLng, new Geocoder(getApplicationContext(), Locale.getDefault()), LocationViewModel.CITY);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            locationViewModel.setLastLocation(LocationServices.FusedLocationApi.getLastLocation(googleApiClient),
                    new Geocoder(getApplicationContext(), Locale.getDefault()), LocationViewModel.CITY);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void setupGoogleClient() {
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

        locationViewModel.setGoogleApiClient(googleApiClient);
    }

    private void setupLocationEditText() {
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
                locationViewModel.getLocationPredictions(s.toString(), AutocompleteFilter.TYPE_FILTER_CITIES, new LatLng(-0, 0), new LatLng(0, 0));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        locationEditText.setOnItemClickListener((parent, view, position, id) -> locationViewModel.onPredictionClick(adapter.getItem(position)));
    }

    private void initViews() {
        nextButton = findViewById(R.id.next_btn);
        locationEditText = findViewById(R.id.choose_location_search_place_edit_text);
    }

}
