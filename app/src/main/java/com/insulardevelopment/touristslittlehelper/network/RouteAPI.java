package com.insulardevelopment.touristslittlehelper.network;

import com.insulardevelopment.touristslittlehelper.network.entities.ArrayResponse;
import com.insulardevelopment.touristslittlehelper.network.entities.ObjectResponse;
import com.insulardevelopment.touristslittlehelper.network.entities.Place.Place;
import com.insulardevelopment.touristslittlehelper.network.entities.RouteResponse;

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

    @GET("directions/json?")
    Observable<RouteResponse> getRoute(@Query("origin") String origin, @Query("waypoints") String waypoints, @Query("destination")String destination,
                                              @Query("mode") String mode, @Query("key") String key);
}
