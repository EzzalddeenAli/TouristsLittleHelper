package com.insulardevelopment.touristslittlehelper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.place.Place;

import java.util.List;

/**
 * Created by Маргарита on 02.02.2017.
 */

public class PlacesPagerAdapter extends FragmentPagerAdapter{

    private static final int num = 2;
    private String[] titles = new String[]{"Список", "Карта"};
    private LatLng latLng;
    private List<Place> places;

    public PlacesPagerAdapter(FragmentManager fragmentManager, LatLng latLng, List<Place> places){
        super(fragmentManager);
        this.latLng = latLng;
        this.places = places;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return PlacesListFragment.newInstance(latLng, places);
            case 1:
                return PlacesMapFragment.newInstance(latLng, places);
            default:
                return PlacesListFragment.newInstance(latLng, places);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return num;
    }
}
