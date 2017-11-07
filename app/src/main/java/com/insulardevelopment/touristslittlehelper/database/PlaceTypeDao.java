package com.insulardevelopment.touristslittlehelper.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.insulardevelopment.touristslittlehelper.model.PlaceType;

import java.util.List;

/**
 * Created by rita on 07.11.2017.
 */

@Dao
public interface PlaceTypeDao {
    @Query("SELECT * FROM place_type")
    LiveData<List<PlaceType>> getAll();

    @Insert
    void insert(PlaceType placeType);

    @Query("UPDATE place_type SET chosen = :chosen  WHERE id = :id")
    void setChosen(int id, boolean chosen);

}
