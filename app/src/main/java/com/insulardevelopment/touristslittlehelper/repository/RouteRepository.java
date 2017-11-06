package com.insulardevelopment.touristslittlehelper.repository;

import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.model.Route;
import com.insulardevelopment.touristslittlehelper.network.RouteAPI;
import com.insulardevelopment.touristslittlehelper.network.entities.RouteResponse;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Маргарита on 06.11.2017.
 */

public class RouteRepository {

    protected RouteAPI routeAPI;

    @Inject
    public RouteRepository(RouteAPI routeAPI) {
        this.routeAPI = routeAPI;
    }

    public Observable<Route> getRoute(List<Place> places, String key){
        String origin = places.get(0).getLatitude() + "," + places.get(0).getLongitude();
        String waypoints = "optimize:true|";
        for(com.insulardevelopment.touristslittlehelper.model.Place place: places){
            if (place == places.get(0) && place == places.get(places.size() - 1)){
                continue;
            }
            waypoints += (place.getLatitude() + "," + place.getLongitude() + "|");
        }
        String destination = places.get(places.size() - 1).getLatitude() + ","
                + places.get(places.size() - 1).getLongitude();
        String mode = "walking";
        return routeAPI.getRoute(origin, waypoints, destination, mode, key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(RouteResponse::getResult)
                .map(list -> list.get(0))
                .map(com.insulardevelopment.touristslittlehelper.network.entities.route.Route::toModelRoute);
    }
}
