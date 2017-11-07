package com.insulardevelopment.touristslittlehelper.di;

import com.insulardevelopment.touristslittlehelper.view.activities.ChoosePlacesActivity;
import com.insulardevelopment.touristslittlehelper.view.activities.MainActivity;
import com.insulardevelopment.touristslittlehelper.view.activities.NewRouteActivity;
import com.insulardevelopment.touristslittlehelper.view.activities.PlaceActivity;
import com.insulardevelopment.touristslittlehelper.view.activities.PlaceTypesActivity;
import com.insulardevelopment.touristslittlehelper.view.activities.StartActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Singleton
@Module
public interface ActivityModule {

    @ContributesAndroidInjector
    MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = PlacesFragmentsModule.class)
    ChoosePlacesActivity contributeChoosePlacesActivity();

    @ContributesAndroidInjector
    NewRouteActivity contributeRouteActivity();

    @ContributesAndroidInjector
    PlaceActivity contributePlaceActivity();

    @ContributesAndroidInjector
    StartActivity contributeStartActivity();

    @ContributesAndroidInjector
    PlaceTypesActivity contributePlaceTypesActivity();

}
