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
    private List<PlaceInfo> placeInfos;
    private AboutPlaceView aboutPlaceView;

    class PlaceInfo{
        private Place place;
        private Marker marker;

        public PlaceInfo() {
        }

        public PlaceInfo(Place place) {
            this.place = place;
        }

        public Place getPlace() {
            return place;
        }

        public void setPlace(Place place) {
            this.place = place;
        }

        public Marker getMarker() {
            return marker;
        }

        public void setMarker(Marker marker) {
            this.marker = marker;
        }

    }

    public PlacesMapFragment(LatLng latLng, List<Place> places){
        super();
        this.selectedLatLng = latLng;
        placeInfos = new ArrayList<>();
        for (Place place: places){
            placeInfos.add(new PlaceInfo(place));
        }
    }

    public  static PlacesMapFragment newInstance(LatLng latLng, List<Place> places){
        return new PlacesMapFragment(latLng, places);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(selectedLatLng, 13.0f ) );
        for(PlaceInfo place: placeInfos){
            if(place.getPlace().getLatitude()!=0) {
                place.setMarker(mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(place.getPlace().getLatitude(), place.getPlace().getLongitude()))
                        .title(place.getPlace().getName())));
            }
        }
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_map, container, false);
        setRetainInstance(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_places);
        initViews(view);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        PlaceInfo placeInfo = null;
        for(PlaceInfo place: placeInfos){
            if (place.getMarker()!=null && place.getMarker().getPosition().equals(marker.getPosition())){
                placeInfo = place;
                break;
            }
        }
        aboutPlaceView.setPlace(placeInfo.getPlace());
        return true;
    }

    private void initViews(View view){
        aboutPlaceView = (AboutPlaceView) view.findViewById(R.id.about_place);
    }
}
