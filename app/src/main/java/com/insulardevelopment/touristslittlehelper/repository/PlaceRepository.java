package com.insulardevelopment.touristslittlehelper.repository;

import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.model.PlaceType;
import com.insulardevelopment.touristslittlehelper.network.RouteAPI;
import com.insulardevelopment.touristslittlehelper.network.entities.ArrayResponse;
import com.insulardevelopment.touristslittlehelper.network.entities.ObjectResponse;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Маргарита on 06.11.2017.
 */

public class PlaceRepository {
    protected RouteAPI routeAPI;

    @Inject
    public PlaceRepository(RouteAPI routeAPI) {
        this.routeAPI = routeAPI;
    }

    public Observable<Place> getPlace(String id, String key){
        return routeAPI.getPlace("ru", id, key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(ObjectResponse::getResult)
                .map(com.insulardevelopment.touristslittlehelper.network.entities.Place.Place::toModelPlace);
    }

    public Observable<List<Place>> getPlaces(LatLng location, int radius, List<PlaceType> types, String key){
        String strTypes = "";
        for (PlaceType type : types) {
            strTypes += (type.getTypesName() + "|");
        }
        strTypes = strTypes.substring(0, strTypes.length() - 1);
        return routeAPI.getPlaces("ru", location.latitude + "," + location.longitude, radius, strTypes, key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(ArrayResponse::getResult)
                .flatMap(Observable::from)
                .map(com.insulardevelopment.touristslittlehelper.network.entities.Place.Place::toModelPlace)
                .toList();
    }
}
