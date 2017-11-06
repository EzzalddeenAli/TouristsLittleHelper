package com.insulardevelopment.touristslittlehelper.di;

import com.insulardevelopment.touristslittlehelper.view.activities.ChoosePlacesActivity;
import com.insulardevelopment.touristslittlehelper.view.activities.NewRouteActivity;
import com.insulardevelopment.touristslittlehelper.view.activities.PlaceActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Singleton
@Module
public interface ActivityModule {
    @ContributesAndroidInjector(modules = PlacesFragmentsModule.class)
    ChoosePlacesActivity contributeChoosePlacesActivity();

    @ContributesAndroidInjector
    NewRouteActivity contributeRouteActivity();

    @ContributesAndroidInjector
    PlaceActivity contributePlaceActivity();
}
