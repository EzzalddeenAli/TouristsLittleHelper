package com.insulardevelopment.touristslittlehelper.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.model.PlaceType;

/**
 * Created by rita on 07.11.2017.
 */


@Database(entities = {Place.class, PlaceType.class, RouteEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PlaceDao placeDao();
    public abstract PlaceTypeDao placeTypeDao();
    public abstract RouteDao routeDao();
}