package com.insulardevelopment.touristslittlehelper.network;

import com.insulardevelopment.touristslittlehelper.network.entities.ArrayResponse;
import com.insulardevelopment.touristslittlehelper.network.entities.ObjectResponse;
import com.insulardevelopment.touristslittlehelper.model.Place;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Маргарита on 24.08.2017.
 */

public interface RouteAPI {

    @GET("place/details/json?")
    Observable<ObjectResponse<Place>> getPlace(@Query("language")String lang, @Query("placeid") String placeId, @Query("key") String key);

    @GET("place/nearbysearch/json?")
    Observable<ArrayResponse<Place>> getPlaces(@Query("language") String lang, @Query("location") String location,
                                               @Query("radius") int radius, @Query("types")String types, @Query("key") String key);
}
