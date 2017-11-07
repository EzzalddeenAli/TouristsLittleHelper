package com.insulardevelopment.touristslittlehelper.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.view.AbstractFragment;
import com.insulardevelopment.touristslittlehelper.view.activities.ChooseStartAndFinishPlaceActivity;
import com.insulardevelopment.touristslittlehelper.view.activities.PlaceActivity;
import com.insulardevelopment.touristslittlehelper.view.adapters.PlaceAdapter;
import com.insulardevelopment.touristslittlehelper.view.viewmodel.ChoosePlacesViewModel;

import java.util.List;

/**
 * Фрагмент для отображения мест в виде списка
 */

public class PlacesListFragment extends AbstractFragment implements PlaceAdapter.OnStartClickListener, PlaceAdapter.OnFinishClickListener {

    private View view;
    private PlaceAdapter placeAdapter;

    private ChoosePlacesViewModel choosePlacesViewModel;

    public static PlacesListFragment newInstance() {
        return new PlacesListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        choosePlacesViewModel = getActivityViewModel(ChoosePlacesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_places_list, container, false);
        setRetainInstance(true);

        setupRecycler(choosePlacesViewModel.getPlaces());
        return view;
    }

    private void setupRecycler(List<Place> places) {
        RecyclerView popularPlacesRecyclerView = view.findViewById(R.id.popular_places_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        placeAdapter = new PlaceAdapter(getActivity(), places,
                (v, position) -> PlaceActivity.start(getActivity(), placeAdapter.getItems().get(position)));
        placeAdapter.setOnStartClickListener(this);
        placeAdapter.setOnFinishClickListener(this);

        popularPlacesRecyclerView.setLayoutManager(layoutManager);
        popularPlacesRecyclerView.setAdapter(placeAdapter);
    }

    @Override
    public void onStartClick(View view, int position) {
        ChooseStartAndFinishPlaceActivity.start(getActivity(), ChooseStartAndFinishPlaceActivity.CHOOSE_START_PLACE, choosePlacesViewModel.getSelectedLatLng());
    }

    @Override
    public void onFinishClick(View view, int position) {
        ChooseStartAndFinishPlaceActivity.start(getActivity(), ChooseStartAndFinishPlaceActivity.CHOOSE_FINISH_PLACE, choosePlacesViewModel.getSelectedLatLng());
    }
}
