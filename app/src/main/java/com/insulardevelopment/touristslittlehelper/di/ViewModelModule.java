package com.insulardevelopment.touristslittlehelper.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.insulardevelopment.touristslittlehelper.view.viewmodel.ViewModelFactory;
import com.insulardevelopment.touristslittlehelper.view.viewmodel.ChoosePlacesViewModel;
import com.insulardevelopment.touristslittlehelper.view.viewmodel.MainViewModel;
import com.insulardevelopment.touristslittlehelper.view.viewmodel.PlaceViewModel;
import com.insulardevelopment.touristslittlehelper.view.viewmodel.RouteViewModel;

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
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
