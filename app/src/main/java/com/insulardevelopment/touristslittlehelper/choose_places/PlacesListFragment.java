package com.insulardevelopment.touristslittlehelper.choose_places;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.place.Place;
import com.insulardevelopment.touristslittlehelper.place.PlaceActivity;
import com.insulardevelopment.touristslittlehelper.place.PlaceAdapter;

import java.util.List;

/**
 * Created by Маргарита on 02.02.2017.
 */

public class PlacesListFragment extends Fragment  implements LocationListener {

    private View view;
    private List<Place> places;
    private PlaceAdapter placeAdapter;
    private Button nextButton;
    private LatLng selectedLatLng;


    public PlacesListFragment(LatLng latLng, List<Place> places){
        super();
        selectedLatLng = latLng;
        this.places = places;
    }

    public static PlacesListFragment newInstance(LatLng latLng, List<Place> places){
        return new PlacesListFragment(latLng, places);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_places_list, container, false);
        setRetainInstance(true);
        placeAdapter = new PlaceAdapter(getActivity(), places, new PlaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PlaceActivity.start(getActivity(), places.get(position));
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        RecyclerView popularPlacesRecyclerView = (RecyclerView) view.findViewById(R.id.popular_places_recycler_view);

        popularPlacesRecyclerView.setLayoutManager(layoutManager);
        popularPlacesRecyclerView.setAdapter(placeAdapter);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}