package com.insulardevelopment.touristslittlehelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.insulardevelopment.touristslittlehelper.place.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Маргарита on 02.02.2017.
 */

public class PlacesMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LatLng selectedLatLng;
    private List<Place> places;
    private List<PlaceInfo> placeInfos;

    class PlaceInfo{
        private Place place;
        private Marker marker;
        private View view;
        private boolean chosen = true;

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

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public boolean isChosen() {
            return chosen;
        }

        public void setChosen(boolean chosen) {
            this.chosen = chosen;
        }
    }

    public PlacesMapFragment(LatLng latLng, List<Place> places){
        super();
        this.places = places;
        this.selectedLatLng = latLng;
        placeInfos = new ArrayList<PlaceInfo>();
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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedLatLng));
        for(PlaceInfo place:placeInfos){
            if(place.getPlace().getLatLng()!=null) {
                place.setMarker(mMap.addMarker(new MarkerOptions().position(place.getPlace().getLatLng()).title(place.getPlace().getName())));
            }
        }
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                PlaceInfo placeInfo = null;
                for(PlaceInfo place: placeInfos){
                    if (place.getMarker()!=null && place.getMarker().getPosition().equals(marker.getPosition())){
                        placeInfo = place;
                        break;
                    }
                }
                if (placeInfo.getView() != null){
                    return placeInfo.getView();
                }
                placeInfo.setView(getActivity().getLayoutInflater().inflate(R.layout.place_marker_layout, null));
                TextView textView = (TextView) placeInfo.getView().findViewById(R.id.marker_place_name_text_view);
                textView.setText(marker.getTitle());
                return placeInfo.getView();
            }
        });
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                PlaceInfo placeInfo = null;
                for(PlaceInfo place: placeInfos){
                    if (place.getMarker()!=null && place.getMarker().getPosition().equals(marker.getPosition())){
                        placeInfo = place;
                        break;
                    }
                }
                CheckBox checkBox = (CheckBox) placeInfo.getView().findViewById(R.id.marker_place_check_box);
                placeInfo.setChosen(!placeInfo.isChosen());
                checkBox.setChecked(placeInfo.isChosen());
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        });
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
        marker.showInfoWindow();
        return true;
    }
}
