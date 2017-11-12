package com.insulardevelopment.touristslittlehelper.ui.choosePlace;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.model.PlaceType;
import com.insulardevelopment.touristslittlehelper.repository.PlaceRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Маргарита on 06.11.2017.
 */

public class ChoosePlacesViewModel extends ViewModel{

    protected PlaceRepository placeRepository;

    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private MutableLiveData<Place> placeLiveData = new MutableLiveData<>();
    private List<Place> places;
    private LatLng selectedLatLng;

    private Place startPlace, finishPlace;

    @Inject
    public ChoosePlacesViewModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public LatLng getSelectedLatLng() {
        return selectedLatLng;
    }

    public void setSelectedLatLng(LatLng selectedLatLng) {
        this.selectedLatLng = selectedLatLng;
    }

    public void setStartPlace(Place startPlace) {
        this.startPlace = startPlace;
    }

    public void setFinishPlace(Place finishPlace) {
        this.finishPlace = finishPlace;
    }

    public MutableLiveData<List<Place>> getPlaces(LatLng location, int radius, List<PlaceType> types, String key){
        MutableLiveData<List<Place>> placesLiveData = new MutableLiveData<>();
        placeRepository.getPlaces(location, radius, types, key).subscribe(placesLiveData::setValue, error -> errorLiveData.setValue(error.getMessage()));
        return placesLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void saveChosenPlaces() {
        List<Place> chosenPlaces = new ArrayList<>();
        for (Place p : places) {
            if (p.isChosen()) {
                chosenPlaces.add(p);
            }
        }
        if (startPlace != null) {
            chosenPlaces.add(0, startPlace);
        }
        if (finishPlace != null) {
            chosenPlaces.add(finishPlace);
        }
        placeRepository.setChosenPlaces(chosenPlaces);
    }

    public MutableLiveData<Place> getPlaceLiveData() {
        return placeLiveData;
    }

    public void updatePlace(Place place) {
        placeLiveData.setValue(place);
    }

    public boolean hasStartOrFinish() {
        return startPlace != null || finishPlace != null;
    }
}
