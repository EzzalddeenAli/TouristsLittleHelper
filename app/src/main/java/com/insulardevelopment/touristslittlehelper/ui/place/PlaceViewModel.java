package com.insulardevelopment.touristslittlehelper.ui.place;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.repository.PlaceRepository;

import javax.inject.Inject;

/**
 * Created by Маргарита on 06.11.2017.
 */

public class PlaceViewModel extends ViewModel{
    protected PlaceRepository placeRepository;

    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    @Inject
    public PlaceViewModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public MutableLiveData<Place> getPlace(String id, String key){
        MutableLiveData<Place> placesLiveData = new MutableLiveData<>();
        placeRepository.getPlace(id, key).subscribe(placesLiveData::setValue, error -> errorLiveData.setValue(error.getMessage()));
        return placesLiveData;
    }
}
