package com.insulardevelopment.touristslittlehelper.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.insulardevelopment.touristslittlehelper.model.Route;

import java.util.List;

/**
 * Created by rita on 07.11.2017.
 */

@Dao
public interface RouteDao {
    @Query("SELECT * FROM route")
    LiveData<List<Route>> getAll();

    @Insert
    long insert(RouteEntity route);

    @Delete
    void delete(RouteEntity route);

    @Query("SELECT * FROM route where id = :id")
    LiveData<Route> getById(int id);
}
