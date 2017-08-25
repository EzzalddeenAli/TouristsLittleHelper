package com.insulardevelopment.touristslittlehelper.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.view.adapters.AutoCompletePredictionAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChooseStartAndFinishPlaceActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String DATA = "data";
    private static final String CODE = "code";
    public static final int CHOOSE_START_PLACE = 1;
    public static final int CHOOSE_FINISH_PLACE = 2;
    private static final String SELECTED_LATLNG = "latlng";

    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private GoogleMap map;
    private Button okBtn, cancelBtn;
    private TextView descriptionTv;
    private AutoCompleteTextView locationEditText;
    private AutoCompletePredictionAdapter adapter;
    private String location;
    private List<AutocompletePrediction> predictions;
    private AutocompletePrediction selected;
    private com.google.android.gms.location.places.Place city;
    private LatLng selectedLatLng, userLatLng;
    private int code;

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
        code = getIntent().getIntExtra(CODE, 0);
        userLatLng = getIntent().getParcelableExtra(SELECTED_LATLNG);
        initViews();
        switch (code){
            case CHOOSE_START_PLACE:
                descriptionTv.setText(R.string.choose_start_place);
                break;
            case CHOOSE_FINISH_PLACE:
                descriptionTv.setText(R.string.choose_finish_place);
                break;
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.choose_start_and_finish_place_map);
        mapFragment.getMapAsync(this);
        predictions = new ArrayList<>();
        adapter = new AutoCompletePredictionAdapter(this, R.layout.autocomplete_prediction_layout);
        adapter.setNotifyOnChange(true);
        okBtn.setOnClickListener(view -> {
            if (selectedLatLng != null) {
                Intent data;
                Place place = new Place();
                place.setGeometry(new Place.Geometry());
                place.getGeometry().setLocation(new Place.Location(selectedLatLng.latitude, selectedLatLng.longitude));
                switch (code){
                    case CHOOSE_START_PLACE:
                        place.setName(Place.START_PLACE);
                        data = new Intent();
                        data.putExtra(DATA, place);
                        setResult(CommonStatusCodes.SUCCESS, data);
                        finish();
                        break;
                    case CHOOSE_FINISH_PLACE:
                        place.setName(Place.FINISH_PLACE);
                        data = new Intent();
                        data.putExtra(DATA, place);
                        setResult(CommonStatusCodes.SUCCESS, data);
                        finish();
                        break;
                }
            } else {
                Toast.makeText(ChooseStartAndFinishPlaceActivity.this, getResources().getText(R.string.no_place_chosen), Toast.LENGTH_SHORT).show();
            }
        });
        cancelBtn.setOnClickListener(view -> finish());
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
                            ChooseStartAndFinishPlaceActivity.this.predictions = predictions;
                            adapter.notifyDataSetChanged();
                        });
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        locationEditText.setOnItemClickListener((parent, view, position, id) -> {
            selected = predictions.get(position);
            Places.GeoDataApi.getPlaceById(googleApiClient, selected.getPlaceId())
                    .setResultCallback(places -> {
                        if (places.getStatus().isSuccess()) {
                            city = places.get(0);
                            LatLng loc = city.getLatLng();
                            map.clear();
                            map.addMarker(new MarkerOptions().position(loc));
                            map.moveCamera(CameraUpdateFactory.newLatLng(loc));
                            selectedLatLng = loc;
                        }
                        places.release();
                    });
        });
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
            googleApiClient.connect();
        }

    }

    private PendingResult<AutocompletePredictionBuffer> getPredictions(String query) {
        LatLngBounds latLngBounds = new LatLngBounds(new LatLng(userLatLng.latitude - 2, userLatLng.longitude - 2), new LatLng(userLatLng.latitude + 2, userLatLng.longitude + 2));
        PendingResult<AutocompletePredictionBuffer> result = Places.GeoDataApi.getAutocompletePredictions(googleApiClient, query, latLngBounds, new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                .build());
        return result;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.animateCamera( CameraUpdateFactory.newLatLngZoom(userLatLng, 13.0f ) );
        map.setOnMapClickListener(latLng -> {
            try {
                Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (addresses.size() > 0) {
                    location = addresses.get(0).getAddressLine(0);
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
    public void onConnected(@Nullable Bundle bundle) {
        try {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = gcd.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1);
                if (addresses.size() > 0) {
                    locationEditText.setText(addresses.get(0).getAddressLine(1));
                }
                location = addresses.get(0).getAddressLine(1);
            }
            LatLng loc = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            map.addMarker(new MarkerOptions().position(loc));
            map.moveCamera(CameraUpdateFactory.newLatLng(loc));
            selectedLatLng = loc;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void initViews(){
        descriptionTv = (TextView) findViewById(R.id.choose_start_and_finish_place_tv);
        okBtn = (Button) findViewById(R.id.choose_start_place_ok_btn);
        cancelBtn = (Button) findViewById(R.id.choose_start_place_cancel_btn);
        locationEditText = (AutoCompleteTextView) findViewById(R.id.choose_start_place_edit_text);
    }
}
