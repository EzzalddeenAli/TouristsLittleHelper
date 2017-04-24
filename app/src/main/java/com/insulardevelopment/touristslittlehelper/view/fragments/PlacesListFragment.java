package com.insulardevelopment.touristslittlehelper.view.fragments;

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
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.view.activities.PlaceActivity;
import com.insulardevelopment.touristslittlehelper.view.adapters.PlaceAdapter;

import java.util.List;

/**
 * Фрагмент для отображения мест в виде списка
 */

public class PlacesListFragment extends Fragment {

    private View view;
    private List<Place> places;
    private PlaceAdapter placeAdapter;


    public PlacesListFragment(List<Place> places){
        super();
        this.places = places;
    }

    public static PlacesListFragment newInstance(List<Place> places){
        return new PlacesListFragment(places);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_places_list, container, false);
        setRetainInstance(true);
        placeAdapter = new PlaceAdapter(getActivity(), places,
                (view1, position) -> PlaceActivity.start(getActivity(), places.get(position)));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        RecyclerView popularPlacesRecyclerView = (RecyclerView) view.findViewById(R.id.popular_places_recycler_view);

        popularPlacesRecyclerView.setLayoutManager(layoutManager);
        popularPlacesRecyclerView.setAdapter(placeAdapter);
        return view;
    }

}
