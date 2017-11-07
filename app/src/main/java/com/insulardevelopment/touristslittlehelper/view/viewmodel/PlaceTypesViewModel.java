package com.insulardevelopment.touristslittlehelper.view.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.PlaceType;
import com.insulardevelopment.touristslittlehelper.repository.PlaceTypeRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by rita on 07.11.2017.
 */

public class PlaceTypesViewModel extends ViewModel {
    @Inject
    PlaceTypeRepository placeTypeRepository;
    
    private List<PlaceType> placeTypes = new ArrayList<>();

    @Inject
    public PlaceTypesViewModel() {
    }

    public void updatePlaceTypes(List<PlaceType> placeTypes){
        for (PlaceType placeType: placeTypes){
            placeTypeRepository.setChosen(placeType);
        }
    }
    
    public void insertPlaceTypes(){
        for (PlaceType placeType: placeTypes){
            placeTypeRepository.insert(placeType);
        }
    }

    public List<PlaceType> getPlaceTypes() {
        String path = "http://maps.gstatic.com/mapfiles/place_api/icons/", size = "-71.png";
        placeTypes.add(new PlaceType(1, "museum", "Музей", false, path + "museum" + size, 0));
        placeTypes.add(new PlaceType(2, "art_gallery", "Галерея", false, path + "art_gallery" + size, 0));
        placeTypes.add(new PlaceType(3, "park", "Парк", false, null, R.drawable.park_icon_400));
        placeTypes.add(new PlaceType(4, "casino", "Казино", false, path + "casino" + size, 0));
        placeTypes.add(new PlaceType(5, "church", "Собор", false, null, R.drawable.curch_icon_360));
        placeTypes.add(new PlaceType(6, "amusement_park", "Парк развлечений", false, null, R.drawable.amusement_park_icon));
        placeTypes.add(new PlaceType(7, "zoo", "Зоопарк", false, path + "zoo" + size, 0));
        placeTypes.add(new PlaceType(8, "stadium", "Стадион", false, path + "stadium" + size, 0));
        placeTypes.add(new PlaceType(9, "aquarium", "Океанариум", false, path + "aquarium" + size, 0));
        
        return placeTypes;
    }

    public LiveData<List<PlaceType>> getSavedPlaceTypes() {
        return placeTypeRepository.getPlaceTypes();
    }
}
