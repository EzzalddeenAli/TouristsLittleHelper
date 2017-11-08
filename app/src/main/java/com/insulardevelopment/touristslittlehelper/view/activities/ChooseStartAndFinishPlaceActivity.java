package com.insulardevelopment.touristslittlehelper.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
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
import com.insulardevelopment.touristslittlehelper.view.AbstractActivity;
import com.insulardevelopment.touristslittlehelper.view.adapters.AutoCompletePredictionAdapter;
import com.insulardevelopment.touristslittlehelper.view.viewmodel.LocationViewModel;

import java.io.IOException;
import java.util.Locale;

public class ChooseStartAndFinishPlaceActivity extends AbstractActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks{

    public static final String DATA = "data";
    private static final String CODE = "code";
    private static final String SELECTED_LATLNG = "latlng";

    private GoogleApiClient googleApiClient;
    private GoogleMap map;
    private Button okBtn, cancelBtn;
    private TextView descriptionTv;
    private AutoCompleteTextView locationEditText;
    private AutoCompletePredictionAdapter adapter;
    private LatLng userLatLng;

    private LocationViewModel locationViewModel;

    public static void start(Context context, int code, LatLng latLng) {
        Intent intent = new Intent(context, ChooseStartAndFinishPlaceActivity.class);
        intent.putExtra(CODE, code);
        intent.putExtra(SELECTED_LATLNG, latLng);
        ((Activity)context).startActivityForResult(intent, code);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_start_and_finish_place);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.choose_start_and_finish_place_map);
        mapFragment.getMapAsync(this);

        locationViewModel = getViewModel(LocationViewModel.class);
        locationViewModel.setCode(getIntent().getIntExtra(CODE, 0));

        initViews();
        descriptionTv.setText(locationViewModel.getTitleResId());

        userLatLng = getIntent().getParcelableExtra(SELECTED_LATLNG);

        locationViewModel.getPredictionsLiveData().observe(this, autocompletePredictions -> {
            adapter.setData(autocompletePredictions);
        });

        locationViewModel.getLatLngLiveData().observe(this, latLng -> {
            map.clear();
            map.addMarker(new MarkerOptions().position(latLng));
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        });

        locationViewModel.getPlaceLiveData().observe(this, s -> {
            locationEditText.setText(s);
        });

        okBtn.setOnClickListener(view -> {
            if (locationViewModel.getSelectedLatLng() != null) {
                setResult(CommonStatusCodes.SUCCESS, new Intent().putExtra(DATA, locationViewModel.getPlace()));
                finish();
            } else {
                Toast.makeText(ChooseStartAndFinishPlaceActivity.this, getResources().getText(R.string.no_place_chosen), Toast.LENGTH_SHORT).show();
            }
        });
        cancelBtn.setOnClickListener(view -> finish());

        setupGoogleClient();
        setupLocationEditText();
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

        locationViewModel.setGoogleApiClient(googleApiClient);
    }

    private void setupLocationEditText() {
        adapter = new AutoCompletePredictionAdapter(this, R.layout.autocomplete_prediction_layout);
        adapter.setNotifyOnChange(true);
        locationEditText.setAdapter(adapter);
        locationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                locationViewModel.getLocationPredictions(s.toString(), AutocompleteFilter.TYPE_FILTER_NONE,
                                                         new LatLng(userLatLng.latitude - 2, userLatLng.longitude - 2),
                                                         new LatLng(userLatLng.latitude + 2, userLatLng.longitude + 2));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        locationEditText.setOnItemClickListener((parent, view, position, id) -> {
            locationViewModel.onPredictionClick(adapter.getItem(position));
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 13.0f ));
        map.setOnMapClickListener(latLng -> {
            try {
                locationViewModel.setLatLng(latLng, new Geocoder(getApplicationContext(), Locale.getDefault()), LocationViewModel.ADDRESS);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            locationViewModel.setLastLocation(LocationServices.FusedLocationApi.getLastLocation(googleApiClient),
                                              new Geocoder(getApplicationContext(), Locale.getDefault()), LocationViewModel.ADDRESS);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void initViews(){
        descriptionTv = findViewById(R.id.choose_start_and_finish_place_tv);
        okBtn = findViewById(R.id.choose_start_place_ok_btn);
        cancelBtn = findViewById(R.id.choose_start_place_cancel_btn);
        locationEditText = findViewById(R.id.choose_start_place_edit_text);
    }
}
