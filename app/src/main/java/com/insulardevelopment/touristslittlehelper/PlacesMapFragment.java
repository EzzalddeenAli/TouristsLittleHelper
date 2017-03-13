package com.insulardevelopment.touristslittlehelper;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.insulardevelopment.touristslittlehelper.place.PlaceActivity;

import java.io.InputStream;
import java.net.URL;
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
 //       mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedLatLng));
        mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(selectedLatLng, 13.0f ) );
        for(PlaceInfo place:placeInfos){
            if(place.getPlace().getLatLng()!=null) {
                place.setMarker(mMap.addMarker(new MarkerOptions().position(place.getPlace().getLatLng()).title(place.getPlace().getName())));
            }
        }

        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_map, container, false);
        setRetainInstance(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_places);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.place_info_rl);
        closeIb = (ImageButton) view.findViewById(R.id.close_ib);
        iconIv = (ImageView) view.findViewById(R.id.place_info_icon_iv);
        placeNameTv = (TextView) view.findViewById(R.id.place_name_info_text_view);
        placeCb = (CheckBox) view.findViewById(R.id.map_choose_place_check_box);
        moreInfoBtn = (Button) view.findViewById(R.id.more_info_place_btn);
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
        final Drawable[] drawable = new Drawable[1];
        final PlaceInfo finalPlaceInfo = placeInfo;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream is = (InputStream) new URL(finalPlaceInfo.getPlace().getIcon()).getContent();
                    drawable[0] = Drawable.createFromStream(is, "src name");
                } catch (Exception e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (drawable[0] != null)
                    iconIv.setImageDrawable(drawable[0]);
            }

        }.execute();
        moreInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceActivity.start(getActivity(), finalPlaceInfo.getPlace());
            }
        });
        closeIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        });
        final PlaceInfo finalPlaceInfo1 = placeInfo;
        placeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                finalPlaceInfo1.getPlace().setChosen(b);
            }
        });
        placeCb.setChecked(finalPlaceInfo1.getPlace().isChosen());
        return true;
    }
}
