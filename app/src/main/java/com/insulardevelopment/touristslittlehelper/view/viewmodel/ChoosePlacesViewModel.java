package com.insulardevelopment.touristslittlehelper.view.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.model.PlaceType;
import com.insulardevelopment.touristslittlehelper.repository.PlaceRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Маргарита on 06.11.2017.
 */

public class ChoosePlacesViewModel extends ViewModel{

    protected PlaceRepository placeRepository;

    private MutableLiveData<String> errorLiveData;

    @Inject
    public ChoosePlacesViewModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public MutableLiveData<List<Place>> getPlaces(LatLng location, int radius, List<PlaceType> types, String key){
        MutableLiveData<List<Place>> placesLiveData = new MutableLiveData<>();
        placeRepository.getPlaces(location, radius, types, key).subscribe(placesLiveData::setValue, error -> errorLiveData.setValue(error.getMessage()));
        return placesLiveData;
    }
}
