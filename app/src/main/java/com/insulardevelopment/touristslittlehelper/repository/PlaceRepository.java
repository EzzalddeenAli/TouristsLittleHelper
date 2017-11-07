package com.insulardevelopment.touristslittlehelper.repository;

import android.arch.persistence.room.Room;

import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.MyApplication;
import com.insulardevelopment.touristslittlehelper.database.AppDatabase;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.model.PlaceType;
import com.insulardevelopment.touristslittlehelper.network.RouteAPI;
import com.insulardevelopment.touristslittlehelper.network.entities.ArrayResponse;
import com.insulardevelopment.touristslittlehelper.network.entities.ObjectResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Маргарита on 06.11.2017.
 */

public class PlaceRepository {
    protected RouteAPI routeAPI;
    private AppDatabase appDatabase;

    @Inject
    public PlaceRepository(RouteAPI routeAPI, MyApplication app) {
        this.routeAPI = routeAPI;
        appDatabase = Room.databaseBuilder(app, AppDatabase.class, "app_database")
                          .build();
    }

    public Observable<Place> getPlace(String id, String key) {
        return routeAPI.getPlace("ru", id, key)
                       .subscribeOn(Schedulers.io())
                       .observeOn(AndroidSchedulers.mainThread())
                       .map(ObjectResponse::getResult)
                       .map(com.insulardevelopment.touristslittlehelper.network.entities.Place.Place::toModelPlace);
    }

    public Single<List<Place>> getPlaces(LatLng location, int radius, List<PlaceType> types, String key) {
        String strTypes = "";
        for (PlaceType type : types) {
            strTypes += (type.getTypesName() + "|");
        }
        strTypes = strTypes.substring(0, strTypes.length() - 1);
        return routeAPI.getPlaces("ru", location.latitude + "," + location.longitude, radius, strTypes, key)
                       .subscribeOn(Schedulers.io())
                       .observeOn(AndroidSchedulers.mainThread())
                       .map(ArrayResponse::getResult)
                       .flatMapIterable(places -> places)
                       .map(place -> place.toModelPlace())
                       .toList();
    }

    public void insert(Place place){
        appDatabase.placeDao().insert(place);
    }
}
