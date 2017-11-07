package com.insulardevelopment.touristslittlehelper.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;

import com.insulardevelopment.touristslittlehelper.MyApplication;
import com.insulardevelopment.touristslittlehelper.database.AppDatabase;
import com.insulardevelopment.touristslittlehelper.database.RouteEntity;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.model.Route;
import com.insulardevelopment.touristslittlehelper.network.RouteAPI;
import com.insulardevelopment.touristslittlehelper.network.entities.RouteResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Маргарита on 06.11.2017.
 */

public class RouteRepository {

    protected RouteAPI routeAPI;
    private AppDatabase appDatabase;

    @Inject
    protected PlaceRepository placeRepository;

    @Inject
    public RouteRepository(RouteAPI routeAPI, MyApplication app) {
        this.routeAPI = routeAPI;
        appDatabase = Room.databaseBuilder(app, AppDatabase.class, "app_database")
                          .build();
    }

    public Observable<Route> getRoute(List<Place> places, String key) {
        String origin = places.get(0)
                              .getLatitude() + "," + places.get(0)
                                                           .getLongitude();
        String waypoints = "optimize:true|";
        for (com.insulardevelopment.touristslittlehelper.model.Place place : places) {
            if (place == places.get(0) && place == places.get(places.size() - 1)) {
                continue;
            }
            waypoints += (place.getLatitude() + "," + place.getLongitude() + "|");
        }
        String destination = places.get(places.size() - 1)
                                   .getLatitude() + "," + places.get(places.size() - 1)
                                                                .getLongitude();
        String mode = "walking";
        return routeAPI.getRoute(origin, waypoints, destination, mode, key)
                       .subscribeOn(Schedulers.io())
                       .observeOn(AndroidSchedulers.mainThread())
                       .map(RouteResponse::getResult)
                       .map(list -> list.get(0))
                       .map(com.insulardevelopment.touristslittlehelper.network.entities.route.Route::toModelRoute);
    }

    public LiveData<List<Route>> getSavedRoutes() {
        return appDatabase.routeDao()
                          .getAll();
    }

    public void insertRoute(Route route) {
        Observable.just(route)
                  .observeOn(Schedulers.io())
                  .subscribe(r -> {
                      long id = appDatabase.routeDao()
                                           .insert(new RouteEntity(r));

                      for (Place place : r.getPlaces()) {
                          place.setRouteId((int) id);
                          placeRepository.insert(place);
                      }
                  });
    }

    public void deleteRoute(Route route) {
        Observable.just(route)
                  .observeOn(Schedulers.io())
                  .subscribe(r -> appDatabase.routeDao()
                                             .delete(new RouteEntity(r)));
    }
}
