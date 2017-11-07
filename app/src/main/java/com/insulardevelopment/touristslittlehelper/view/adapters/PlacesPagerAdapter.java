package com.insulardevelopment.touristslittlehelper.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.insulardevelopment.touristslittlehelper.view.fragments.PlacesListFragment;
import com.insulardevelopment.touristslittlehelper.view.fragments.PlacesMapFragment;

/**
 * PagerAdapter для отображения мест
 */

public class PlacesPagerAdapter extends FragmentPagerAdapter{

    private static final int num = 2;
    private String[] titles = new String[]{"Список", "Карта"};

    public PlacesPagerAdapter(FragmentManager fragmentManager) {
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
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return num;
    }
}
