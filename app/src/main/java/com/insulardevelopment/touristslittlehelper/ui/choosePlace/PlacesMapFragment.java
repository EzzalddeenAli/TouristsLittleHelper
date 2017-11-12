package com.insulardevelopment.touristslittlehelper.ui.choosePlace;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.ui.AboutPlaceView;
import com.insulardevelopment.touristslittlehelper.ui.AbstractFragment;

/**
 * Фрагмент для отображения мест в на карте
 */

public class PlacesMapFragment extends AbstractFragment implements OnMapReadyCallback {

    private GoogleMap map;
    private AboutPlaceView aboutPlaceView;

    private ChoosePlacesViewModel choosePlacesViewModel;
    private PlacesMapViewModel placesMapViewModel;

    public static PlacesMapFragment newInstance() {
        return new PlacesMapFragment();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(choosePlacesViewModel.getSelectedLatLng(), 13.0f));
        for (Place place : choosePlacesViewModel.getPlaces()) {
            if (place.getLatitude() != 0) {
                placesMapViewModel.setMarkerAndPlace(map.addMarker(new MarkerOptions()
                        .position(new LatLng(place.getLatitude(), place.getLongitude()))
                        .title(place.getName())), place);
            }
        }

        map.setOnMarkerClickListener(marker -> placesMapViewModel.onMarkerClick(marker));
        placesMapViewModel.getPlaceMutableLiveData().observe(this, place -> aboutPlaceView.setPlace(place));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        choosePlacesViewModel = getActivityViewModel(ChoosePlacesViewModel.class);
        placesMapViewModel = getViewModel(PlacesMapViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_map, container, false);
        setRetainInstance(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_places);
        initViews(view);
        aboutPlaceView.setOnCheckedChangeListener(p -> choosePlacesViewModel.updatePlace(p));

        choosePlacesViewModel.getPlaceLiveData().observe(this, place -> aboutPlaceView.updatePlace(place));

        mapFragment.getMapAsync(this);
        return view;
    }

    private void initViews(View view){
        aboutPlaceView = view.findViewById(R.id.about_place);
    }
}
