package com.insulardevelopment.touristslittlehelper.ui.route;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.model.Route;
import com.insulardevelopment.touristslittlehelper.repository.PlaceRepository;
import com.insulardevelopment.touristslittlehelper.repository.RouteRepository;
import com.insulardevelopment.touristslittlehelper.util.GeoUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Маргарита on 06.11.2017.
 */

public class RouteViewModel extends ViewModel{
    protected RouteRepository routeRepository;
    protected PlaceRepository placeRepository;

    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private List<Place> places;

    @Inject
    public RouteViewModel(RouteRepository routeRepository, PlaceRepository placeRepository) {
        this.routeRepository = routeRepository;
        this.placeRepository = placeRepository;
    }

    public MutableLiveData<Route> getRoute(List<Place> places, String key){
        MutableLiveData<Route> routeLiveData = new MutableLiveData<>();
        routeRepository.getRoute(places, key).subscribe(routeLiveData::setValue, error -> errorLiveData.setValue(error.getMessage()));
        return routeLiveData;
    }

    public List<Place> getPlaces(boolean hasStartOrFinish) {
        places = placeRepository.getChosenPlaces();
        GeoUtil.changePlacesOrder(places, hasStartOrFinish);
        return places;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LatLng getLatLng() {
        return new LatLng(places.get(0).getLatitude(), places.get(0).getLongitude());
    }

    public void saveRoute(Route route) {
        route.setPlaces(places);
        routeRepository.insertRoute(route);
    }
}
