package com.insulardevelopment.touristslittlehelper.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;

import com.insulardevelopment.touristslittlehelper.MyApplication;
import com.insulardevelopment.touristslittlehelper.database.AppDatabase;
import com.insulardevelopment.touristslittlehelper.model.PlaceType;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by rita on 07.11.2017.
 */

public class PlaceTypeRepository {
    private AppDatabase appDatabase;

    @Inject
    public PlaceTypeRepository(MyApplication app) {
        appDatabase = Room.databaseBuilder(app, AppDatabase.class, "app_database")
                          .build();
    }

    public void insert(PlaceType placeType) {
        Observable.just(placeType)
                  .observeOn(Schedulers.io())
                  .subscribe(p -> appDatabase.placeTypeDao()
                                             .insert(p));
    }

    public void setChosen(PlaceType placeType) {
        Observable.just(placeType)
                  .observeOn(Schedulers.io())
                  .subscribe(p -> appDatabase.placeTypeDao()
                                             .setChosen(placeType.getId(), placeType.isChosen()));
    }

    public LiveData<List<PlaceType>> getPlaceTypes() {
        return appDatabase.placeTypeDao()
                          .getAll();
    }
}
