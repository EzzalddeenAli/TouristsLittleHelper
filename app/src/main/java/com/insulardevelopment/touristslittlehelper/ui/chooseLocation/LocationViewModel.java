package com.insulardevelopment.touristslittlehelper.ui.chooseLocation;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Place;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by rita on 08.11.2017.
 */

public class LocationViewModel extends ViewModel {

    public static final int ADDRESS = 0;
    public static final int CITY = 1;

    public static final int CHOOSE_START_PLACE = 1;
    public static final int CHOOSE_FINISH_PLACE = 2;

    private GoogleApiClient googleApiClient;
    private MutableLiveData<List<AutocompletePrediction>> predictionsLiveData = new MutableLiveData<>();
    private MutableLiveData<LatLng> latLngLiveData = new MutableLiveData<>();
    private MutableLiveData<String> placeLiveData = new MutableLiveData<>();
    private LatLng selectedLatLng;

    private int code;

    @Inject
    public LocationViewModel(){}

    public void getLocationPredictions(String s, int filter, LatLng start, LatLng finish){
        Observable.just(s)
                  .map(str -> getPredictions(str, filter, start, finish))
                  .map(res -> res.await(60, TimeUnit.SECONDS))
                  .flatMapIterable(predictions -> predictions)
                  .toList()
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribeOn(Schedulers.io())
                  .subscribe(predictions -> predictionsLiveData.setValue(predictions));
    }

    public MutableLiveData<List<AutocompletePrediction>> getPredictionsLiveData() {
        return predictionsLiveData;
    }

    public MutableLiveData<LatLng> getLatLngLiveData() {
        return latLngLiveData;
    }

    public MutableLiveData<String> getPlaceLiveData() {
        return placeLiveData;
    }

    public LatLng getSelectedLatLng() {
        return selectedLatLng;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    public void setLastLocation(Location lastLocation, Geocoder gcd, int code) throws IOException {
        if (lastLocation != null) {
            setLatLng(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), gcd, code);
        }
    }

    public void setLatLng(LatLng latLng, Geocoder gcd, int code) throws IOException {
        List<Address> addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
        if (addresses.size() > 0) {
            placeLiveData.setValue(addresses.get(0).getAddressLine(code));
        }
        selectedLatLng = latLng;
        latLngLiveData.setValue(selectedLatLng);
    }

    private PendingResult<AutocompletePredictionBuffer> getPredictions(String query, int filter, LatLng start, LatLng finish) {
        LatLngBounds latLngBounds = new LatLngBounds(start, finish);
        return Places.GeoDataApi.getAutocompletePredictions(googleApiClient, query, latLngBounds, new AutocompleteFilter.Builder()
                .setTypeFilter(filter)
                .build());
    }

    public void onPredictionClick(AutocompletePrediction autocompletePrediction){
        Places.GeoDataApi.getPlaceById(googleApiClient, autocompletePrediction.getPlaceId())
                         .setResultCallback(places -> {
                             if (places.getStatus().isSuccess()) {
                                 selectedLatLng = places.get(0).getLatLng();
                                 latLngLiveData.setValue(selectedLatLng);
                             }
                             places.release();
                         });
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Place getPlace(){
        Place place = new Place();
        place.setLatitude(selectedLatLng.latitude);
        place.setLongitude(selectedLatLng.longitude);
        place.setName(code == CHOOSE_START_PLACE ? Place.START_PLACE : Place.FINISH_PLACE);
        return place;
    }

    public int getTitleResId(){
        return code == CHOOSE_START_PLACE ? R.string.choose_start_place : R.string.choose_finish_place;
    }
}
