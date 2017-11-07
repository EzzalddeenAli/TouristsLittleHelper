package com.insulardevelopment.touristslittlehelper.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.view.AboutPlaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Фрагмент для отображения мест в на карте
 */

public class PlacesMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LatLng selectedLatLng;
    private List<Place> places;
    private List<Marker> markers;
    private AboutPlaceView aboutPlaceView;

    public PlacesMapFragment(LatLng latLng, List<Place> places){
        super();
        this.selectedLatLng = latLng;
        this.places = places;
    }

    public  static PlacesMapFragment newInstance(LatLng latLng, List<Place> places){
        return new PlacesMapFragment(latLng, places);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(selectedLatLng, 13.0f ) );
        for(Place place: places){
            if(place.getLatitude()!=0) {
                markers.add(mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(place.getLatitude(), place.getLongitude()))
                        .title(place.getName())));
            }
        }
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_map, container, false);
        setRetainInstance(true);
        markers = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_places);
        initViews(view);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for(Place place: places){
            Marker m = markers.get(places.indexOf(place));
            if (m!=null && m.getPosition().equals(marker.getPosition())){
                aboutPlaceView.setPlace(place);
                return true;
            }
        }
        return false;
    }

    private void initViews(View view){
        aboutPlaceView = view.findViewById(R.id.about_place);
    }
}
