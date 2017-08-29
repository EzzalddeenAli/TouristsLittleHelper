package com.insulardevelopment.touristslittlehelper.network;

import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.model.PlaceType;
import com.insulardevelopment.touristslittlehelper.network.entities.route.Route;
import com.insulardevelopment.touristslittlehelper.network.entities.ArrayResponse;
import com.insulardevelopment.touristslittlehelper.network.entities.ObjectResponse;
import com.insulardevelopment.touristslittlehelper.network.entities.Place.Place;
import com.insulardevelopment.touristslittlehelper.network.entities.RouteResponse;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Маргарита on 24.08.2017.
 */

public class APIWorker {

    private static final String SERVER_ADDRESS = "https://maps.googleapis.com/maps/api/";

    private static RouteAPI routeAPI;

    static {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_ADDRESS)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        routeAPI = retrofit.create(RouteAPI.class);
    }

    public static Observable<com.insulardevelopment.touristslittlehelper.model.Place> getPlace(String id, String key){
        return routeAPI.getPlace("ru", id, key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(ObjectResponse::getResult)
                .map(Place::toModelPlace);
    }

    public static Observable<List<com.insulardevelopment.touristslittlehelper.model.Place>> getPlaces(LatLng location, int radius, List<PlaceType> types, String key){
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
                .map(Place::toModelPlace)
                .toList();
    }

    public static Observable<Route> getRoute(List<com.insulardevelopment.touristslittlehelper.model.Place> places, String key){
        String origin = places.get(0).getLatitude() + "," + places.get(0).getLongitude();
        String waypoints = "optimize:true|";
        for(com.insulardevelopment.touristslittlehelper.model.Place place: places){
            if (place == places.get(0) && place == places.get(places.size() - 1)){
                continue;
            }
            waypoints += (place.getLatitude() + "," + place.getLongitude() + "|");
        }
        String destination = places.get(places.size() - 1).getLatitude() + ","
                + places.get(places.size() - 1).getLongitude();
        String mode = "walking";
        return routeAPI.getRoute(origin, waypoints, destination, mode, key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(RouteResponse::getResult)
                .map(list -> list.get(0));
    }
}
