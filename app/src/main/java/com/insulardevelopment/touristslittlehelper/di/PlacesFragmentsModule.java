package com.insulardevelopment.touristslittlehelper.di;

import com.insulardevelopment.touristslittlehelper.ui.choosePlace.PlacesListFragment;
import com.insulardevelopment.touristslittlehelper.ui.choosePlace.PlacesMapFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Маргарита on 06.11.2017.
 */

@Module
public interface PlacesFragmentsModule {
    @ContributesAndroidInjector
    PlacesListFragment contributePlacesListFragment();

    @ContributesAndroidInjector
    PlacesMapFragment contributePlacesMapFragment();
}
