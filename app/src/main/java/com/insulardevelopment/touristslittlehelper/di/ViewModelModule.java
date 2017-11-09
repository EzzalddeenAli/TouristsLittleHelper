package com.insulardevelopment.touristslittlehelper.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.insulardevelopment.touristslittlehelper.ui.ViewModelFactory;
import com.insulardevelopment.touristslittlehelper.ui.chooseLocation.LocationViewModel;
import com.insulardevelopment.touristslittlehelper.ui.choosePlace.ChoosePlacesViewModel;
import com.insulardevelopment.touristslittlehelper.ui.choosePlace.PlacesMapViewModel;
import com.insulardevelopment.touristslittlehelper.ui.main.MainViewModel;
import com.insulardevelopment.touristslittlehelper.ui.place.PlaceViewModel;
import com.insulardevelopment.touristslittlehelper.ui.placeType.PlaceTypesViewModel;
import com.insulardevelopment.touristslittlehelper.ui.route.RouteViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PlaceViewModel.class)
    abstract ViewModel bindPlaceViewModel(PlaceViewModel placeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChoosePlacesViewModel.class)
    abstract ViewModel bindChoosePlacesViewModel(ChoosePlacesViewModel choosePlacesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RouteViewModel.class)
    abstract ViewModel bindRouteViewModel(RouteViewModel routeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PlaceTypesViewModel.class)
    abstract ViewModel bindPlaceTypesViewModel(PlaceTypesViewModel placeTypesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PlacesMapViewModel.class)
    abstract ViewModel bindPlacesMapViewModel(PlacesMapViewModel placesMapViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LocationViewModel.class)
    abstract ViewModel bindLocationViewModel(LocationViewModel locationViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
