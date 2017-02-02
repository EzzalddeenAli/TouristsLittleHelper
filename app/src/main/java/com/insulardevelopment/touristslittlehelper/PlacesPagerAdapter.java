package com.insulardevelopment.touristslittlehelper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

/**
 * Created by Маргарита on 02.02.2017.
 */

public class PlacesPagerAdapter extends FragmentPagerAdapter{

    private static final int num = 2;

    public PlacesPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return PlacesListFragment.newInstance();
            case 1:
                return PlacesMapFragment.newInstance();
            default:
                return PlacesListFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return num;
    }
}
