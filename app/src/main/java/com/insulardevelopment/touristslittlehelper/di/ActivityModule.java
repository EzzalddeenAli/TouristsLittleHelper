package com.insulardevelopment.touristslittlehelper.di;

import com.insulardevelopment.touristslittlehelper.ui.chooseLocation.ChooseLocationActivity;
import com.insulardevelopment.touristslittlehelper.ui.chooseLocation.ChooseStartAndFinishPlaceActivity;
import com.insulardevelopment.touristslittlehelper.ui.choosePlace.ChoosePlacesActivity;
import com.insulardevelopment.touristslittlehelper.ui.main.MainActivity;
import com.insulardevelopment.touristslittlehelper.ui.place.PlaceActivity;
import com.insulardevelopment.touristslittlehelper.ui.placeType.PlaceTypesActivity;
import com.insulardevelopment.touristslittlehelper.ui.placeType.StartActivity;
import com.insulardevelopment.touristslittlehelper.ui.route.NewRouteActivity;
import com.insulardevelopment.touristslittlehelper.ui.route.RouteActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Singleton
@Module
public interface ActivityModule {

    @ContributesAndroidInjector
    MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    ChooseLocationActivity contributeChooseLocationActivity();

    @ContributesAndroidInjector(modules = PlacesFragmentsModule.class)
    ChoosePlacesActivity contributeChoosePlacesActivity();

    @ContributesAndroidInjector
    NewRouteActivity contributeNewRouteActivity();

    @ContributesAndroidInjector
    RouteActivity contributeRouteActivity();

    @ContributesAndroidInjector
    PlaceActivity contributePlaceActivity();

    @ContributesAndroidInjector
    ChooseStartAndFinishPlaceActivity contributeChooseStartAndFinishPlaceActivity();

    @ContributesAndroidInjector
    StartActivity contributeStartActivity();

    @ContributesAndroidInjector
    PlaceTypesActivity contributePlaceTypesActivity();

}
