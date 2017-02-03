package com.insulardevelopment.touristslittlehelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.insulardevelopment.touristslittlehelper.place.Place;

import java.util.List;

/**
 * Created by Маргарита on 02.02.2017.
 */

public class PlacesMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LatLng selectedLatLng;
    private List<Place> places;

    public PlacesMapFragment(LatLng latLng, List<Place> places){
        super();
        this.places = places;
        this.selectedLatLng = latLng;
    }

    public  static PlacesMapFragment newInstance(LatLng latLng, List<Place> places){
        return new PlacesMapFragment(latLng, places);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedLatLng));
        for(Place place:places){
            if(place.getLatLng()!=null) {
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName()));//.showInfoWindow();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_map, container, false);
        setRetainInstance(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_places);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
