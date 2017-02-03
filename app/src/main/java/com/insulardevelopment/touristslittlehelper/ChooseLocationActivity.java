package com.insulardevelopment.touristslittlehelper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/*
*   Активити для выбора города
*/

public class ChooseLocationActivity extends FragmentActivity implements OnMapReadyCallback,  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private GoogleMap mMap;
    private Button nextButton;
    private AutoCompleteTextView locationEditText;
    private AutoCompletePredictionAdapter adapter;
    private String location;
    private List<AutocompletePrediction> predictions;
    private AutocompletePrediction selected;
    private Place city;
    private LatLng selectedLatLng;

    class PredictionsResult extends AsyncTask<String, Void, ArrayList<AutocompletePrediction>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected ArrayList<AutocompletePrediction> doInBackground(String... params) {
            LatLngBounds latLngBounds = new LatLngBounds(new LatLng(-0,0), new LatLng(0,0));
            PendingResult<AutocompletePredictionBuffer> result = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, params[0].toString(), latLngBounds, new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .build());
            AutocompletePredictionBuffer autocompletePredictions = result.await(60, TimeUnit.SECONDS);
            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList<AutocompletePrediction>(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                // Get the details of this prediction and copy it into a new PlaceAutocomplete object.
                resultList.add(prediction);
            }
            return resultList;
        }

        @Override
        protected void onPostExecute(ArrayList<AutocompletePrediction> result) {
            adapter.clear();
            predictions.clear();
            for (AutocompletePrediction prediction : result) {
                adapter.add(prediction);
                predictions.add(prediction);
            }
        }
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
                //PopularPlacesActivity.start(ChooseLocationActivity.this, location);
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
                PredictionsResult predictionsResult = new PredictionsResult();
                predictionsResult.execute(s.toString());
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
                                    mMap.clear();
                                    mMap.addMarker(new MarkerOptions().position(loc));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
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





    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                try {
                    Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses.size() > 0)
                        locationEditText.setText(addresses.get(0).getAddressLine(1));
                    location = addresses.get(0).getAddressLine(1);
                    locationEditText.setText(location);
                    selectedLatLng = latLng;
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng));
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
            mMap.addMarker(new MarkerOptions().position(loc));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
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
