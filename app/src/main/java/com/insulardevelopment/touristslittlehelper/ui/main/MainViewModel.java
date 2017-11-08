package com.insulardevelopment.touristslittlehelper.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.insulardevelopment.touristslittlehelper.model.Route;
import com.insulardevelopment.touristslittlehelper.repository.PlaceTypeRepository;
import com.insulardevelopment.touristslittlehelper.repository.RouteRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Маргарита on 06.11.2017.
 */

public class MainViewModel extends ViewModel{

    @Inject
    PlaceTypeRepository placeTypeRepository;
    @Inject
    RouteRepository routeRepository;

    @Inject
    public MainViewModel() {
    }

    public LiveData<List<Route>> getRoutes(){
        return routeRepository.getSavedRoutes();
    }

    public void deleteRoute(Route route){
        routeRepository.deleteRoute(route);
    }
}
