package com.insulardevelopment.touristslittlehelper.view.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.model.Route;
import com.insulardevelopment.touristslittlehelper.repository.RouteRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Маргарита on 06.11.2017.
 */

public class RouteViewModel extends ViewModel{
    protected RouteRepository routeRepository;

    private MutableLiveData<String> errorLiveData;

    @Inject
    public RouteViewModel(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }


    public MutableLiveData<Route> getRoute(List<Place> places, String key){
        MutableLiveData<Route> routeLiveData = new MutableLiveData<>();
        routeRepository.getRoute(places, key).subscribe(routeLiveData::setValue, error -> errorLiveData.setValue(error.getMessage()));
        return routeLiveData;
    }
}
