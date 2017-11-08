package com.insulardevelopment.touristslittlehelper.ui.choosePlace;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.android.gms.maps.model.Marker;
import com.insulardevelopment.touristslittlehelper.model.Place;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by Маргарита on 07.11.2017.
 */

public class PlacesMapViewModel extends ViewModel {

    private HashMap<Marker, Place> placeHashMap;

    private MutableLiveData<Place> placeMutableLiveData;

    @Inject
    public PlacesMapViewModel() {
        placeHashMap = new HashMap<>();
        placeMutableLiveData = new MutableLiveData<>();
    }

    public void setMarkerAndPlace(Marker marker, Place place) {
        placeHashMap.put(marker, place);
    }

    public boolean onMarkerClick(Marker marker) {
        if (placeHashMap.containsKey(marker)) {
            placeMutableLiveData.setValue(placeHashMap.get(marker));
            return true;
        }
        return false;
    }

    public MutableLiveData<Place> getPlaceMutableLiveData() {
        return placeMutableLiveData;
    }
}
