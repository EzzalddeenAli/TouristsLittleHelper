package com.insulardevelopment.touristslittlehelper.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

import com.insulardevelopment.touristslittlehelper.model.Place;

/**
 * Created by rita on 07.11.2017.
 */

@Dao
public interface PlaceDao {
    @Insert
    void insert(Place place);

    @Delete
    void delete(Place place);
}
