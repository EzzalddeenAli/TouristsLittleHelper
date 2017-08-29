package com.insulardevelopment.touristslittlehelper.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.view.activities.PlaceActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Фрагмент для отображения мест в на карте
 */

public class PlacesMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LatLng selectedLatLng;
    private List<PlaceInfo> placeInfos;
    private RelativeLayout relativeLayout;
    private ImageView iconIv;
    private ImageButton closeIb;
    private TextView placeNameTv;
    private CheckBox placeCb;
    private Button moreInfoBtn;

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
        relativeLayout.setVisibility(View.VISIBLE);
        PlaceInfo placeInfo = null;
        for(PlaceInfo place: placeInfos){
            if (place.getMarker()!=null && place.getMarker().getPosition().equals(marker.getPosition())){
                placeInfo = place;
                break;
            }
        }
        placeNameTv.setText(placeInfo.getPlace().getName());
        Glide.with(getActivity())
                .load(placeInfo.getPlace().getIcon())
                .into(iconIv);
        final PlaceInfo finalPlaceInfo = placeInfo;
        moreInfoBtn.setOnClickListener(view -> PlaceActivity.start(getActivity(), finalPlaceInfo.getPlace()));
        closeIb.setOnClickListener(view -> relativeLayout.setVisibility(View.INVISIBLE));
        placeCb.setOnCheckedChangeListener((compoundButton, b) -> finalPlaceInfo.getPlace().setChosen(b));
        placeCb.setChecked(finalPlaceInfo.getPlace().isChosen());
        return true;
    }

    private void initViews(View view){
        relativeLayout = (RelativeLayout) view.findViewById(R.id.place_info_rl);
        closeIb = (ImageButton) view.findViewById(R.id.close_ib);
        iconIv = (ImageView) view.findViewById(R.id.place_info_icon_iv);
        placeNameTv = (TextView) view.findViewById(R.id.place_name_info_text_view);
        placeCb = (CheckBox) view.findViewById(R.id.map_choose_place_check_box);
        moreInfoBtn = (Button) view.findViewById(R.id.more_info_place_btn);
    }
}
